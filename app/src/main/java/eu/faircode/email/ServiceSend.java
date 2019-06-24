package eu.faircode.email;

/*
    This file is part of FairEmail.

    FairEmail is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FairEmail is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FairEmail.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2018-2019 by Marcel Bokhorst (M66B)
*/

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.PowerManager;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class ServiceSend extends LifecycleService {
    private int lastUnsent = 0;
    private boolean lastSuitable = false;
    private TwoStateOwner cowner;

    private static boolean booted = false;
    private ExecutorService executor = Executors.newSingleThreadExecutor(Helper.backgroundThreadFactory);

    private static final int IDENTITY_ERROR_AFTER = 30; // minutes

    @Override
    public void onCreate() {
        Log.i("Service send create");
        super.onCreate();

        cowner = new TwoStateOwner(ServiceSend.this, "send");
        final DB db = DB.getInstance(this);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        // Observe unsent count
        db.operation().liveUnsent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer unsent) {
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(Helper.NOTIFICATION_SEND, getNotificationService(unsent, null).build());
            }
        });

        // Observe send operations
        db.operation().liveOperations(null).observe(cowner, new Observer<List<EntityOperation>>() {
            private List<Long> handling = new ArrayList<>();
            private PowerManager.WakeLock wlFolder = pm.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, BuildConfig.APPLICATION_ID + ":send");

            @Override
            public void onChanged(final List<EntityOperation> operations) {
                boolean process = false;
                List<Long> ops = new ArrayList<>();
                for (EntityOperation op : operations) {
                    if (!handling.contains(op.id))
                        process = true;
                    ops.add(op.id);
                }
                handling = ops;

                if (handling.size() > 0 && process) {
                    Log.i("OUTBOX operations=" + operations.size());

                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                wlFolder.acquire();

                                EntityFolder outbox = db.folder().getOutbox();
                                try {
                                    db.folder().setFolderError(outbox.id, null);
                                    db.folder().setFolderSyncState(outbox.id, "syncing");

                                    List<EntityOperation> ops = db.operation().getOperations(outbox.id);
                                    Log.i(outbox.name + " pending operations=" + ops.size());
                                    for (EntityOperation op : ops) {
                                        EntityMessage message = null;
                                        try {
                                            Log.i(outbox.name +
                                                    " start op=" + op.id + "/" + op.name +
                                                    " msg=" + op.message +
                                                    " args=" + op.args);

                                            switch (op.name) {
                                                case EntityOperation.SYNC:
                                                    db.folder().setFolderError(outbox.id, null);
                                                    break;

                                                case EntityOperation.SEND:
                                                    message = db.message().getMessage(op.message);
                                                    if (message == null)
                                                        throw new MessageRemovedException();
                                                    send(message);
                                                    break;

                                                default:
                                                    throw new IllegalArgumentException("Unknown operation=" + op.name);
                                            }

                                            db.operation().deleteOperation(op.id);
                                        } catch (Throwable ex) {
                                            Log.e(outbox.name, ex);

                                            db.operation().setOperationError(op.id, Helper.formatThrowable(ex));
                                            if (message != null)
                                                db.message().setMessageError(message.id, Helper.formatThrowable(ex));

                                            if (ex instanceof OutOfMemoryError ||
                                                    ex instanceof MessageRemovedException ||
                                                    ex instanceof SendFailedException ||
                                                    ex instanceof IllegalArgumentException) {
                                                Log.w("Unrecoverable");
                                                db.operation().deleteOperation(op.id);
                                                continue;
                                            } else
                                                throw ex;
                                        } finally {
                                            Log.i(outbox.name + " end op=" + op.id + "/" + op.name);
                                        }

                                        if (!ConnectionHelper.getNetworkState(ServiceSend.this).isSuitable())
                                            break;
                                    }

                                    if (db.operation().getOperations(outbox.id).size() == 0)
                                        stopSelf();

                                } catch (Throwable ex) {
                                    Log.e(outbox.name, ex);
                                    db.folder().setFolderError(outbox.id, Helper.formatThrowable(ex));
                                } finally {
                                    db.folder().setFolderState(outbox.id, null);
                                    db.folder().setFolderSyncState(outbox.id, null);
                                }

                            } finally {
                                wlFolder.release();
                            }
                        }
                    });
                }
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        cm.registerNetworkCallback(builder.build(), networkCallback);
    }

    @Override
    public void onDestroy() {
        Log.i("Service send destroy");

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.unregisterNetworkCallback(networkCallback);

        stopForeground(true);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(Helper.NOTIFICATION_SEND);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(Helper.NOTIFICATION_SEND, getNotificationService(null, null).build());

        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    NotificationCompat.Builder getNotificationService(Integer unsent, Boolean suitable) {
        if (unsent != null)
            lastUnsent = unsent;
        if (suitable != null)
            lastSuitable = suitable;

        // Build pending intent
        Intent intent = new Intent(this, ActivityView.class);
        intent.setAction("outbox");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(
                this, ActivityView.REQUEST_OUTBOX, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "send")
                        .setSmallIcon(R.drawable.baseline_send_24)
                        .setContentTitle(getString(R.string.title_notification_sending))
                        .setContentIntent(pi)
                        .setAutoCancel(false)
                        .setShowWhen(true)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setCategory(NotificationCompat.CATEGORY_STATUS)
                        .setVisibility(NotificationCompat.VISIBILITY_SECRET);

        if (lastUnsent > 0)
            builder.setContentText(getResources().getQuantityString(
                    R.plurals.title_notification_unsent, lastUnsent, lastUnsent));
        if (!lastSuitable)
            builder.setSubText(getString(R.string.title_notification_waiting));

        return builder;
    }

    ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            Log.i("Service send available=" + network);
            check();
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities caps) {
            Log.i("Service send network=" + network + " caps=" + caps);
            check();
        }

        private void check() {
            boolean suitable = ConnectionHelper.getNetworkState(ServiceSend.this).isSuitable();
            Log.i("OUTBOX suitable=" + suitable);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(Helper.NOTIFICATION_SEND, getNotificationService(null, suitable).build());

            if (suitable)
                cowner.start();
            else
                cowner.stop();
        }
    };

    private void send(EntityMessage message) throws MessagingException, IOException {
        DB db = DB.getInstance(this);

        // Mark attempt
        if (message.last_attempt == null) {
            message.last_attempt = new Date().getTime();
            db.message().setMessageLastAttempt(message.id, message.last_attempt);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean debug = prefs.getBoolean("debug", false);

        if (message.identity == null)
            throw new IllegalArgumentException("Identity removed");

        EntityIdentity ident = db.identity().getIdentity(message.identity);
        String protocol = ident.getProtocol();

        // Get properties
        Properties props = MessageHelper.getSessionProperties(ident.realm, ident.insecure);

        String haddr;
        if (ident.use_ip) {
            InetAddress addr = InetAddress.getByName(ident.host);
            if (addr instanceof Inet4Address)
                haddr = "[" + Inet4Address.getLocalHost().getHostAddress() + "]";
            else {
                // Inet6Address.getLocalHost() will return the IPv6 local host
                byte[] LOOPBACK = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
                haddr = "[IPv6:" + Inet6Address.getByAddress("ip6-localhost", LOOPBACK, 0).getHostAddress() + "]";
            }
        } else
            haddr = ident.host;

        EntityLog.log(this, "Send localhost=" + haddr);
        props.put("mail." + protocol + ".localhost", haddr);

        // Create session
        final Session isession = Session.getInstance(props, null);
        isession.setDebug(debug);

        // Create message
        MimeMessage imessage = MessageHelper.from(this, message, ident, isession);

        // Add reply to
        if (ident.replyto != null)
            imessage.setReplyTo(new Address[]{new InternetAddress(ident.replyto)});

        // Add bcc
        if (ident.bcc != null) {
            List<Address> bcc = new ArrayList<>();
            Address[] existing = imessage.getRecipients(Message.RecipientType.BCC);
            if (existing != null)
                bcc.addAll(Arrays.asList(existing));
            bcc.add(new InternetAddress(ident.bcc));
            imessage.setRecipients(Message.RecipientType.BCC, bcc.toArray(new Address[0]));
        }

        if (message.receipt_request == null || !message.receipt_request) {
            // defacto standard
            if (ident.delivery_receipt)
                imessage.addHeader("Return-Receipt-To", ident.replyto == null ? ident.email : ident.replyto);

            // https://tools.ietf.org/html/rfc3798
            if (ident.read_receipt)
                imessage.addHeader("Disposition-Notification-To", ident.replyto == null ? ident.email : ident.replyto);
        }

        // Create transport
        // TODO: cache transport?
        try (Transport itransport = isession.getTransport(protocol)) {
            // Connect transport
            db.identity().setIdentityState(ident.id, "connecting");
            itransport.connect(ident.host, ident.port, ident.user, ident.password);
            db.identity().setIdentityState(ident.id, "connected");

            // Send message
            Address[] to = imessage.getAllRecipients();
            itransport.sendMessage(imessage, to);
            long time = new Date().getTime();
            EntityLog.log(this, "Sent via " + ident.host + "/" + ident.user +
                    " to " + TextUtils.join(", ", to));

            // Append replied/forwarded text
            StringBuilder sb = new StringBuilder();
            sb.append(Helper.readText(message.getFile(this)));
            if (!TextUtils.isEmpty(ident.signature))
                sb.append(ident.signature);
            File refFile = message.getRefFile(this);
            if (refFile.exists())
                sb.append(Helper.readText(refFile));
            Helper.writeText(message.getFile(this), sb.toString());

            try {
                db.beginTransaction();

                db.message().setMessageIdentity(message.id, null);
                db.message().setMessageSent(message.id, time);
                db.message().setMessageReceiptRequested(message.id, ident.delivery_receipt || ident.read_receipt);
                db.message().setMessageSeen(message.id, true);
                db.message().setMessageUiSeen(message.id, true);
                db.message().setMessageError(message.id, null);

                EntityFolder sent = db.folder().getFolderByType(message.account, EntityFolder.SENT);
                if (ident.store_sent && sent != null) {
                    db.message().setMessageFolder(message.id, sent.id);
                    message.folder = sent.id;
                    EntityOperation.queue(this, message, EntityOperation.ADD);
                } else {
                    if (!BuildConfig.DEBUG && !debug)
                        db.message().setMessageUiHide(message.id, new Date().getTime());
                }

                if (message.inreplyto != null) {
                    List<EntityMessage> replieds = db.message().getMessageByMsgId(message.account, message.inreplyto);
                    for (EntityMessage replied : replieds)
                        EntityOperation.queue(this, replied, EntityOperation.ANSWERED, true);
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

            if (refFile.exists())
                refFile.delete();

            db.identity().setIdentityConnected(ident.id, new Date().getTime());
            db.identity().setIdentityError(ident.id, null);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel("send:" + message.identity, 1);
        } catch (MessagingException ex) {
            Log.e(ex);

            db.identity().setIdentityError(ident.id, Helper.formatThrowable(ex));

            if (ex instanceof AuthenticationFailedException ||
                    ex instanceof SendFailedException) {
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify("send:" + message.identity, 1,
                        Core.getNotificationError(this, "error", ident.name, ex)
                                .build());
                throw ex;
            }

            long now = new Date().getTime();
            long delayed = now - message.last_attempt;
            if (delayed > IDENTITY_ERROR_AFTER * 60 * 1000L || ex instanceof SendFailedException) {
                Log.i("Reporting send error after=" + delayed);
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify("send:" + message.identity, 1,
                        Core.getNotificationError(this, "warning", ident.name, ex).build());
            }

            throw ex;
        } finally {
            db.identity().setIdentityState(ident.id, null);
        }
    }

    static void boot(final Context context) {
        if (!booted) {
            booted = true;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DB db = DB.getInstance(context);
                        EntityFolder outbox = db.folder().getOutbox();
                        if (outbox != null && db.operation().getOperations(outbox.id).size() > 0)
                            start(context);
                    } catch (Throwable ex) {
                        Log.e(ex);
                    }
                }
            }, "send:boot");
            thread.setPriority(THREAD_PRIORITY_BACKGROUND);
            thread.start();
        }
    }

    static void start(Context context) {
        ContextCompat.startForegroundService(context,
                new Intent(context, ServiceSend.class));
    }
}
