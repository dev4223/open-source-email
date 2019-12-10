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

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import com.sun.mail.imap.IMAPFolder;

import java.net.SocketException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.ReadOnlyFolderException;
import javax.mail.StoreClosedException;
import javax.mail.event.FolderAdapter;
import javax.mail.event.FolderEvent;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class ServiceSynchronize extends ServiceBase implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Boolean lastSuitable = null;
    private int lastAccounts = 0;
    private int lastOperations = 0;

    private static final int CONNECT_BACKOFF_START = 8; // seconds
    private static final int CONNECT_BACKOFF_MAX = 64; // seconds (totally 2 minutes)
    private static final int CONNECT_BACKOFF_AlARM = 15; // minutes
    private static final int ACCOUNT_ERROR_AFTER = 60; // minutes
    private static final int BACKOFF_ERROR_AFTER = 16; // seconds

    private static final List<String> PREF_EVAL = Collections.unmodifiableList(Arrays.asList(
            "enabled", "poll_interval"
    ));

    private static final List<String> PREF_RELOAD = Collections.unmodifiableList(Arrays.asList(
            "metered", "roaming", "rlah", // force reconnect
            "socks_enabled", "socks_proxy", // force reconnect
            "subscribed_only", // force folder sync
            "badge", "unseen_ignored", // force update badge/widget
            "debug" // force reconnect
    ));

    static final int PI_ALARM = 1;
    static final int PI_WAKEUP = 2;

    private MutableLiveData<ConnectionHelper.NetworkState> liveNetworkState = new MutableLiveData<>();
    private MutableLiveData<List<TupleAccountState>> liveAccountState = new MutableLiveData<>();
    private MediatorState liveAccountNetworkState = new MediatorState();

    private class MediatorState extends MediatorLiveData<List<TupleAccountNetworkState>> {
        boolean running = true;
        private ConnectionHelper.NetworkState lastNetworkState = null;
        private List<TupleAccountState> lastAccountStates = null;

        private void post(Bundle command) {
            Log.i("### command posted");
            for (String extra : Log.getExtras(command))
                Log.i("### " + extra);
            post(command, lastNetworkState, lastAccountStates);
        }

        private void post(ConnectionHelper.NetworkState networkState) {
            lastNetworkState = networkState;
            post(null, lastNetworkState, lastAccountStates);
        }

        private void post(List<TupleAccountState> accountStates) {
            lastAccountStates = accountStates;
            post(null, lastNetworkState, lastAccountStates);
        }

        private void postDestroy() {
            if (running) {
                running = false;
                postValue(null);
            }
        }

        private void post(Bundle command, ConnectionHelper.NetworkState networkState, List<TupleAccountState> accountStates) {
            if (!running) {
                Log.i("### not running");
                return;
            }

            if (networkState == null || accountStates == null)
                return;

            if (Looper.myLooper() == Looper.getMainLooper())
                _post(command, networkState, accountStates);
            else {
                // Some Android versions call onDestroy not on the main thread
                Log.e("### not main thread states=" + accountStates.size());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        _post(command, networkState, accountStates);
                    }
                });
            }
        }

        private void _post(Bundle command, ConnectionHelper.NetworkState networkState, List<TupleAccountState> accountStates) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ServiceSynchronize.this);
            boolean enabled = prefs.getBoolean("enabled", true);
            int pollInterval = prefs.getInt("poll_interval", 0);

            long[] schedule = getSchedule(ServiceSynchronize.this);
            long now = new Date().getTime();
            boolean scheduled = (schedule == null || now >= schedule[0] && now < schedule[1]);

            Long account = null;
            if (command != null) {
                account = command.getLong("account", -1);
                if (account < 0)
                    account = null;
            }

            List<TupleAccountNetworkState> result = new ArrayList<>();
            for (TupleAccountState accountState : accountStates)
                if (account == null || account.equals(accountState.id))
                    result.add(new TupleAccountNetworkState(
                            enabled && pollInterval == 0 && scheduled,
                            command,
                            networkState,
                            accountState));

            postValue(result);
        }
    }

    @Override
    public void onCreate() {
        EntityLog.log(this, "Service create version=" + BuildConfig.VERSION_NAME);
        super.onCreate();
        startForeground(Helper.NOTIFICATION_SYNCHRONIZE, getNotificationService(null, null).build());

        // Listen for network changes
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        // Removed because of Android VPN service
        // builder.addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        cm.registerNetworkCallback(builder.build(), networkCallback);

        IntentFilter iif = new IntentFilter();
        iif.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        iif.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(connectionChangedReceiver, iif);
        registerReceiver(onScreenOff, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        DB db = DB.getInstance(this);

        db.account().liveAccountState().observe(this, new Observer<List<TupleAccountState>>() {
            @Override
            public void onChanged(List<TupleAccountState> accountStates) {
                liveAccountState.postValue(accountStates);
            }
        });

        liveAccountNetworkState.addSource(liveNetworkState, new Observer<ConnectionHelper.NetworkState>() {
            @Override
            public void onChanged(ConnectionHelper.NetworkState networkState) {
                liveAccountNetworkState.post(networkState);
            }
        });

        liveAccountNetworkState.addSource(liveAccountState, new Observer<List<TupleAccountState>>() {
            @Override
            public void onChanged(List<TupleAccountState> accountStates) {
                liveAccountNetworkState.post(accountStates);
            }
        });

        liveAccountNetworkState.observeForever(new Observer<List<TupleAccountNetworkState>>() {
            private List<TupleAccountNetworkState> accountStates = new ArrayList<>();
            private Map<TupleAccountNetworkState, Core.State> serviceStates = new Hashtable<>();
            private ExecutorService queue = Helper.getBackgroundExecutor(1, "service");

            @Override
            public void onChanged(List<TupleAccountNetworkState> accountNetworkStates) {
                if (accountNetworkStates == null) {
                    // Destroy
                    for (TupleAccountNetworkState prev : serviceStates.keySet())
                        stop(prev);

                    quit();

                    accountStates.clear();
                    serviceStates.clear();
                    liveAccountNetworkState.removeObserver(this);
                } else {
                    int accounts = 0;
                    int operations = 0;
                    boolean runService = false;
                    for (TupleAccountNetworkState current : accountNetworkStates) {
                        if (current.accountState.shouldRun(current.enabled))
                            runService = true;
                        if ("connected".equals(current.accountState.state))
                            accounts++;
                        if (current.accountState.synchronize)
                            operations += current.accountState.operations;

                        int index = accountStates.indexOf(current);
                        if (index < 0) {
                            if (current.canRun()) {
                                EntityLog.log(ServiceSynchronize.this, "### new " + current);
                                start(current, current.accountState.isEnabled(current.enabled));
                            }
                        } else {
                            TupleAccountNetworkState prev = accountStates.get(index);
                            accountStates.remove(index);

                            Core.State state = serviceStates.get(current);
                            if (state != null)
                                state.setNetworkState(current.networkState);

                            boolean reload = false;
                            if (current.command != null)
                                switch (current.command.getString("name")) {
                                    case "reload":
                                        reload = true;
                                        break;
                                    case "wakeup":
                                        if (state == null)
                                            Log.e("### wakeup without state");
                                        else
                                            state.release();
                                        continue;
                                }

                            // Some networks disallow email server connections:
                            // - reload on network type change when disconnected
                            if (reload ||
                                    prev.canRun() != current.canRun() ||
                                    !prev.accountState.equals(current.accountState) ||
                                    (!"connected".equals(current.accountState.state) &&
                                            !Objects.equals(prev.networkState.getType(), current.networkState.getType()))) {
                                if (prev.canRun() || current.canRun())
                                    EntityLog.log(ServiceSynchronize.this, "### changed " + current +
                                            " reload=" + reload +
                                            " stop=" + prev.canRun() +
                                            " start=" + current.canRun() +
                                            " changed=" + !prev.accountState.equals(current.accountState) +
                                            " enabled=" + current.accountState.synchronize +
                                            " state=" + current.accountState.state +
                                            " type=" + prev.networkState.getType() + "/" + current.networkState.getType());
                                if (prev.canRun())
                                    stop(prev);
                                if (current.canRun())
                                    start(current, current.accountState.isEnabled(current.enabled));
                                if (current.accountState.tbd != null)
                                    delete(current);
                            }
                        }

                        if (current.accountState.tbd == null)
                            accountStates.add(current);
                    }

                    if (runService) {
                        if (lastAccounts != accounts || lastOperations != operations) {
                            lastAccounts = accounts;
                            lastOperations = operations;
                            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.notify(Helper.NOTIFICATION_SYNCHRONIZE, getNotificationService(lastAccounts, lastOperations).build());
                        }
                    } else
                        stopSelf(); // will result in quit
                }
            }

            private void start(final TupleAccountNetworkState accountNetworkState, boolean sync) {
                EntityLog.log(ServiceSynchronize.this, "Service start=" + accountNetworkState);

                final Core.State astate = new Core.State(accountNetworkState.networkState);
                astate.runnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            monitorAccount(accountNetworkState.accountState, astate, sync);
                        } catch (Throwable ex) {
                            Log.e(accountNetworkState.accountState.name, ex);
                        }
                    }
                }, "sync.account." + accountNetworkState.accountState.id);
                serviceStates.put(accountNetworkState, astate);

                queue.submit(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> crumb = new HashMap<>();
                        crumb.put("account", accountNetworkState.accountState.id.toString());
                        crumb.put("connected", Boolean.toString(accountNetworkState.networkState.isConnected()));
                        crumb.put("suitable", Boolean.toString(accountNetworkState.networkState.isSuitable()));
                        crumb.put("unmetered", Boolean.toString(accountNetworkState.networkState.isUnmetered()));
                        crumb.put("roaming", Boolean.toString(accountNetworkState.networkState.isRoaming()));
                        Log.breadcrumb("start", crumb);

                        Log.i("### start=" + accountNetworkState);
                        astate.start();
                        EntityLog.log(ServiceSynchronize.this, "### started=" + accountNetworkState);
                    }
                });
            }

            private void stop(final TupleAccountNetworkState accountNetworkState) {
                EntityLog.log(ServiceSynchronize.this, "Service stop=" + accountNetworkState);

                final Core.State state = serviceStates.get(accountNetworkState);
                serviceStates.remove(accountNetworkState);

                queue.submit(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> crumb = new HashMap<>();
                        crumb.put("account", accountNetworkState.accountState.id.toString());
                        crumb.put("connected", Boolean.toString(accountNetworkState.networkState.isConnected()));
                        crumb.put("suitable", Boolean.toString(accountNetworkState.networkState.isSuitable()));
                        crumb.put("unmetered", Boolean.toString(accountNetworkState.networkState.isUnmetered()));
                        crumb.put("roaming", Boolean.toString(accountNetworkState.networkState.isRoaming()));
                        Log.breadcrumb("stop", crumb);

                        Log.i("### stop=" + accountNetworkState);
                        state.stop();
                        state.join();
                        EntityLog.log(ServiceSynchronize.this, "### stopped=" + accountNetworkState);
                    }
                });
            }

            private void delete(final TupleAccountNetworkState accountNetworkState) {
                EntityLog.log(ServiceSynchronize.this, "Service delete=" + accountNetworkState);

                queue.submit(new Runnable() {
                    @Override
                    public void run() {
                        DB db = DB.getInstance(ServiceSynchronize.this);
                        db.account().deleteAccount(accountNetworkState.accountState.id);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.deleteNotificationChannel(EntityAccount.getNotificationChannelId(accountNetworkState.accountState.id));
                        }
                    }
                });
            }

            private void quit() {
                EntityLog.log(ServiceSynchronize.this, "Service quit");

                queue.submit(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("### quit");

                        DB db = DB.getInstance(ServiceSynchronize.this);
                        List<EntityOperation> ops = db.operation().getOperations(EntityOperation.SYNC);
                        for (EntityOperation op : ops)
                            db.folder().setFolderSyncState(op.folder, null);

                        stopSelf();
                        EntityLog.log(ServiceSynchronize.this, "### quited");
                    }
                });
            }
        });

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        db.message().liveUnseenWidget().observe(this, new Observer<TupleMessageStats>() {
            private Integer lastUnseen = null;

            @Override
            public void onChanged(TupleMessageStats stats) {
                if (stats == null)
                    stats = new TupleMessageStats();

                boolean unseen_ignored = prefs.getBoolean("unseen_ignored", false);
                Integer unseen = (unseen_ignored ? stats.notifying : stats.unseen);
                if (unseen == null)
                    unseen = 0;

                if (lastUnseen == null || !lastUnseen.equals(unseen)) {
                    Log.i("Stats " + stats);
                    lastUnseen = unseen;
                    setUnseen(unseen);
                }
            }
        });

        final TwoStateOwner cowner = new TwoStateOwner(this, "liveUnseenNotify");

        db.folder().liveSynchronizing().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                Log.i("Synchronizing folders=" + count);
                if (count == null || count == 0)
                    cowner.start();
                else
                    cowner.stop();
            }
        });

        Map<Long, List<Long>> groupNotifying = new HashMap<>();

        // Get existing notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            try {
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                for (StatusBarNotification sbn : nm.getActiveNotifications()) {
                    String tag = sbn.getTag();
                    if (tag != null && tag.startsWith("unseen.")) {
                        String[] p = tag.split(("\\."));
                        long group = Long.parseLong(p[1]);
                        long id = sbn.getNotification().extras.getLong("id", 0);

                        if (!groupNotifying.containsKey(group))
                            groupNotifying.put(group, new ArrayList<>());

                        if (id > 0) {
                            Log.i("Notify restore " + tag + " id=" + id);
                            groupNotifying.get(group).add(id);
                        }
                    }
                }
            } catch (Throwable ex) {
                Log.w(ex);
                /*
                    java.lang.RuntimeException: Unable to create service eu.faircode.email.ServiceSynchronize: java.lang.NullPointerException: Attempt to invoke virtual method 'java.util.List android.content.pm.ParceledListSlice.getList()' on a null object reference
                            at android.app.ActivityThread.handleCreateService(ActivityThread.java:2944)
                            at android.app.ActivityThread.access$1900(ActivityThread.java:154)
                            at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1474)
                            at android.os.Handler.dispatchMessage(Handler.java:102)
                            at android.os.Looper.loop(Looper.java:234)
                            at android.app.ActivityThread.main(ActivityThread.java:5526)
                */
            }

        db.message().liveUnseenNotify().observe(cowner, new Observer<List<TupleMessageEx>>() {
            private ExecutorService executor =
                    Helper.getBackgroundExecutor(1, "notify");

            @Override
            public void onChanged(final List<TupleMessageEx> messages) {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Core.notifyMessages(ServiceSynchronize.this, messages, groupNotifying);
                        } catch (SecurityException ex) {
                            Log.w(ex);
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ServiceSynchronize.this);
                            prefs.edit().remove("sound").apply();
                        } catch (Throwable ex) {
                            Log.e(ex);
                        }
                    }
                });
            }
        });

        db.message().liveWidgetUnified().observe(cowner, new Observer<List<TupleMessageWidgetCount>>() {
            private List<TupleMessageWidgetCount> last = null;

            @Override
            public void onChanged(List<TupleMessageWidgetCount> current) {
                if (current == null)
                    current = new ArrayList<>();

                boolean changed = false;
                if (last == null || last.size() != current.size())
                    changed = true;
                else
                    for (int i = 0; i < current.size(); i++)
                        if (!current.get(i).equals(last.get(i))) {
                            changed = true;
                            break;
                        }

                if (changed)
                    WidgetUnified.update(ServiceSynchronize.this);

                last = current;
            }
        });

        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (PREF_EVAL.contains(key)) {
            Bundle command = new Bundle();
            command.putString("name", "eval");
        } else if (PREF_RELOAD.contains(key)) {
            Bundle command = new Bundle();
            command.putString("name", "reload");
            liveAccountNetworkState.post(command);
        }
    }

    @Override
    public void onDestroy() {
        EntityLog.log(this, "Service destroy");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);

        unregisterReceiver(onScreenOff);
        unregisterReceiver(connectionChangedReceiver);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.unregisterNetworkCallback(networkCallback);

        liveAccountNetworkState.postDestroy();

        setUnseen(null);

        try {
            stopForeground(true);
        } catch (Throwable ex) {
            Log.e(ex);
/*
            OnePlus A6013 - Android 9

            java.lang.RuntimeException: Unable to stop service eu.faircode.email.ServiceSynchronize@3995fc9: java.lang.NullPointerException: Attempt to invoke virtual method 'long java.lang.Long.longValue()' on a null object reference
                    at android.app.ActivityThread.handleStopService(ActivityThread.java:3908)
                    at android.app.ActivityThread.access$1900(ActivityThread.java:209)
                    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1826)
                    at android.os.Handler.dispatchMessage(Handler.java:106)
                    at android.os.Looper.loop(Looper.java:193)
                    at android.app.ActivityThread.main(ActivityThread.java:6954)
                    at java.lang.reflect.Method.invoke(Method.java:-2)
                    at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:537)
                    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:858)
            Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'long java.lang.Long.longValue()' on a null object reference
                    at android.os.Parcel.createException(Parcel.java:1956)
                    at android.os.Parcel.readException(Parcel.java:1918)
                    at android.os.Parcel.readException(Parcel.java:1868)
                    at android.app.IActivityManager$Stub$Proxy.setServiceForeground(IActivityManager.java:5111)
                    at android.app.Service.stopForeground(Service.java:724)
                    at android.app.Service.stopForeground(Service.java:710)
*/
        }

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(Helper.NOTIFICATION_SYNCHRONIZE);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = (intent == null ? null : intent.getAction());
        String reason = (intent == null ? null : intent.getStringExtra("reason"));
        EntityLog.log(ServiceSynchronize.this, "### Service command " + intent +
                " action=" + action + " reason=" + reason);
        Log.logExtras(intent);

        super.onStartCommand(intent, flags, startId);
        startForeground(Helper.NOTIFICATION_SYNCHRONIZE, getNotificationService(null, null).build());

        if (action != null)
            try {
                String[] a = action.split(":");
                Bundle command = new Bundle();
                switch (a[0]) {
                    case "eval":
                        command.putString("name", "eval");
                        command.putLong("account", intent.getLongExtra("account", -1));
                        liveAccountNetworkState.post(command);
                        break;

                    case "reload":
                        command.putString("name", "reload");
                        command.putLong("account", intent.getLongExtra("account", -1));
                        liveAccountNetworkState.post(command);
                        break;

                    case "wakeup":
                        command.putString("name", "wakeup");
                        command.putLong("account", Long.parseLong(a[1]));
                        liveAccountNetworkState.post(command);
                        break;

                    case "alarm":
                        schedule(this);
                        command.putString("name", "eval");
                        liveAccountNetworkState.post(command);
                        break;

                    default:
                        Log.w("Unknown action: " + action);
                }
            } catch (Throwable ex) {
                Log.e(ex);
            }

        return START_STICKY;
    }

    private NotificationCompat.Builder getNotificationService(Integer accounts, Integer operations) {
        if (accounts != null)
            this.lastAccounts = accounts;
        if (operations != null)
            this.lastOperations = operations;

        // Build pending intent
        Intent why = new Intent(this, ActivityView.class);
        why.setAction("why");
        PendingIntent piWhy = PendingIntent.getActivity(this, ActivityView.REQUEST_WHY, why, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "service")
                        .setSmallIcon(R.drawable.baseline_compare_arrows_white_24)
                        .setContentTitle(getResources().getQuantityString(
                                R.plurals.title_notification_synchronizing, lastAccounts, lastAccounts))
                        .setContentIntent(piWhy)
                        .setAutoCancel(false)
                        .setShowWhen(false)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setCategory(NotificationCompat.CATEGORY_SERVICE)
                        .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                        .setLocalOnly(true);

        if (lastOperations > 0)
            builder.setContentText(getResources().getQuantityString(
                    R.plurals.title_notification_operations, lastOperations, lastOperations));

        if (lastSuitable == null || !lastSuitable)
            builder.setSubText(getString(R.string.title_notification_waiting));

        return builder;
    }

    private void setUnseen(Integer unseen) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean badge = prefs.getBoolean("badge", true);

        Widget.update(this, unseen);

        try {
            if (unseen == null || !badge)
                ShortcutBadger.removeCount(this);
            else
                ShortcutBadger.applyCount(this, unseen);
        } catch (Throwable ex) {
            Log.e(ex);
        }
    }

    private void monitorAccount(final EntityAccount account, final Core.State state, final boolean sync) throws NoSuchProviderException {
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wlAccount = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, BuildConfig.APPLICATION_ID + ":account." + account.id);
        final PowerManager.WakeLock wlFolder = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, BuildConfig.APPLICATION_ID + ":account." + account.id + ".folder");
        final PowerManager.WakeLock wlMessage = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, BuildConfig.APPLICATION_ID + ":account." + account.id + ".message");

        try {
            wlAccount.acquire();

            PendingIntent piWakeup = PendingIntent.getService(
                    this,
                    PI_WAKEUP,
                    new Intent("wakeup:" + account.id),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (account.notify)
                    account.createNotificationChannel(ServiceSynchronize.this);
                else
                    account.deleteNotificationChannel(ServiceSynchronize.this);
            }

            final DB db = DB.getInstance(this);

            int backoff = CONNECT_BACKOFF_START;
            while (state.isRunning()) {
                state.reset();
                Log.i(account.name + " run");

                Handler handler = new Handler(getMainLooper());
                final List<TwoStateOwner> cowners = new ArrayList<>();

                // Debug
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                boolean debug = (prefs.getBoolean("debug", false) || BuildConfig.DEBUG);

                final MailService iservice = new MailService(
                        this, account.getProtocol(), account.realm, account.insecure, false, debug);
                iservice.setPartialFetch(account.partial_fetch);
                iservice.setIgnoreBodyStructureSize(account.ignore_size);
                if (account.protocol != EntityAccount.TYPE_IMAP)
                    iservice.setLeaveOnServer(account.browse);
                iservice.setListener(new StoreListener() {
                    @Override
                    public void notification(StoreEvent e) {
                        if (e.getMessageType() == StoreEvent.NOTICE)
                            EntityLog.log(ServiceSynchronize.this, account.name + " notice: " + e.getMessage());
                        else
                            try {
                                wlFolder.acquire();

                                String message = e.getMessage();
                                Log.w(account.name + " alert: " + message);
                                EntityLog.log(
                                        ServiceSynchronize.this, account.name + " " +
                                                Log.formatThrowable(new Core.AlertException(message), false));
                                db.account().setAccountError(account.id, message);

                                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                nm.notify("alert:" + account.id, 1,
                                        Core.getNotificationError(
                                                ServiceSynchronize.this, "warning", account.name,
                                                new Core.AlertException(message))
                                                .build());

                                state.error(null);
                            } finally {
                                wlFolder.release();
                            }
                    }
                });

                final Map<EntityFolder, IMAPFolder> mapFolders = new HashMap<>();
                List<Thread> idlers = new ArrayList<>();
                try {
                    // Initiate connection
                    EntityLog.log(this, account.name + " connecting");
                    db.folder().setFolderStates(account.id, null);
                    db.account().setAccountState(account.id, "connecting");

                    try {
                        iservice.connect(account);
                    } catch (Throwable ex) {
                        // Immediately report auth errors
                        if (ex instanceof AuthenticationFailedException &&
                                !(ex.getCause() instanceof SocketException)) {
                            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.notify("receive:" + account.id, 1,
                                    Core.getNotificationError(this, "error", account.name, ex)
                                            .build());
                            throw ex;
                        }

                        // Report account connection error
                        if (account.last_connected != null && !ConnectionHelper.airplaneMode(this)) {
                            EntityLog.log(this, account.name + " last connected: " + new Date(account.last_connected));

                            long now = new Date().getTime();
                            long delayed = now - account.last_connected - account.poll_interval * 60 * 1000L;
                            if (delayed > ACCOUNT_ERROR_AFTER * 60 * 1000L && backoff > BACKOFF_ERROR_AFTER) {
                                Log.i("Reporting sync error after=" + delayed);
                                Throwable warning = new Throwable(
                                        getString(R.string.title_no_sync,
                                                Helper.getDateTimeInstance(this, DateFormat.SHORT, DateFormat.SHORT)
                                                        .format(account.last_connected)), ex);
                                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                nm.notify("receive:" + account.id, 1,
                                        Core.getNotificationError(this, "warning", account.name, warning)
                                                .build());
                            }
                        }

                        throw ex;
                    }

                    final boolean capIdle = iservice.hasCapability("IDLE");
                    Log.i(account.name + " idle=" + capIdle);

                    db.account().setAccountState(account.id, "connected");
                    db.account().setAccountError(account.id, null);
                    db.account().setAccountWarning(account.id, null);
                    EntityLog.log(this, account.name + " connected");

                    // Listen for folder events
                    iservice.getStore().addFolderListener(new FolderAdapter() {
                        @Override
                        public void folderCreated(FolderEvent e) {
                            try {
                                wlFolder.acquire();

                                String name = e.getFolder().getFullName();
                                Log.i("Folder created=" + name);
                                if (db.folder().getFolderByName(account.id, name) == null)
                                    reload(ServiceSynchronize.this, account.id, "folder created");
                            } finally {
                                wlFolder.release();
                            }
                        }

                        @Override
                        public void folderRenamed(FolderEvent e) {
                            try {
                                wlFolder.acquire();

                                String old = e.getFolder().getFullName();
                                String name = e.getNewFolder().getFullName();
                                Log.i("Folder renamed from=" + old + " to=" + name);

                                int count = db.folder().renameFolder(account.id, old, name);
                                Log.i("Renamed to " + name + " count=" + count);
                                if (count == 0)
                                    reload(ServiceSynchronize.this, account.id, "folder renamed");
                            } finally {
                                wlFolder.release();
                            }
                        }

                        @Override
                        public void folderDeleted(FolderEvent e) {
                            try {
                                wlFolder.acquire();

                                String name = e.getFolder().getFullName();
                                Log.i("Folder deleted=" + name);
                                if (db.folder().getFolderByName(account.id, name) != null)
                                    reload(ServiceSynchronize.this, account.id, "folder deleted");
                            } finally {
                                wlFolder.release();
                            }
                        }
                    });

                    // Update folder list
                    if (account.protocol == EntityAccount.TYPE_IMAP)
                        Core.onSynchronizeFolders(this, account, iservice.getStore(), state);

                    // Open synchronizing folders
                    final ExecutorService executor =
                            Helper.getBackgroundExecutor(1, "account_" + account.id);

                    List<EntityFolder> folders = db.folder().getFolders(account.id, false, true);
                    Collections.sort(folders, new Comparator<EntityFolder>() {
                        @Override
                        public int compare(EntityFolder f1, EntityFolder f2) {
                            int s1 = EntityFolder.FOLDER_SORT_ORDER.indexOf(f1.type);
                            int s2 = EntityFolder.FOLDER_SORT_ORDER.indexOf(f2.type);
                            int s = Integer.compare(s1, s2);
                            if (s != 0)
                                return s;

                            return f1.name.compareTo(f2.name);
                        }
                    });

                    for (final EntityFolder folder : folders) {
                        if (folder.synchronize && !folder.poll && capIdle && sync) {
                            Log.i(account.name + " sync folder " + folder.name);

                            db.folder().setFolderState(folder.id, "connecting");

                            final IMAPFolder ifolder = (IMAPFolder) iservice.getStore().getFolder(folder.name);
                            try {
                                if (BuildConfig.DEBUG && "Postausgang".equals(folder.name))
                                    throw new ReadOnlyFolderException(ifolder);
                                ifolder.open(Folder.READ_WRITE);
                                db.folder().setFolderReadOnly(folder.id, ifolder.getUIDNotSticky());
                            } catch (ReadOnlyFolderException ex) {
                                Log.w(folder.name + " read only");
                                try {
                                    ifolder.open(Folder.READ_ONLY);
                                    db.folder().setFolderReadOnly(folder.id, true);
                                } catch (Throwable ex1) {
                                    db.folder().setFolderError(folder.id, Log.formatThrowable(ex1));
                                    throw ex1;
                                }
                            } catch (FolderNotFoundException ex) {
                                Log.w(folder.name, ex);
                                db.folder().deleteFolder(folder.id);
                                continue;
                            } catch (Throwable ex) {
                                db.folder().setFolderError(folder.id, Log.formatThrowable(ex));
                                throw ex;
                            }
                            mapFolders.put(folder, ifolder);

                            db.folder().setFolderState(folder.id, "connected");
                            db.folder().setFolderError(folder.id, null);

                            int count = ifolder.getMessageCount();
                            db.folder().setFolderTotal(folder.id, count < 0 ? null : count);

                            Log.i(account.name + " folder " + folder.name + " flags=" + ifolder.getPermanentFlags());

                            // Listen for new and deleted messages
                            ifolder.addMessageCountListener(new MessageCountAdapter() {
                                @Override
                                public void messagesAdded(MessageCountEvent e) {
                                    try {
                                        wlMessage.acquire();
                                        Log.i(folder.name + " messages added");

                                        for (Message imessage : e.getMessages()) {
                                            long uid = ifolder.getUID(imessage);
                                            EntityOperation.queue(ServiceSynchronize.this, folder, EntityOperation.FETCH, uid);
                                        }
                                    } catch (Throwable ex) {
                                        Log.e(folder.name, ex);
                                        EntityLog.log(
                                                ServiceSynchronize.this,
                                                folder.name + " " + Log.formatThrowable(ex, false));
                                        state.error(ex);
                                    } finally {
                                        wlMessage.release();
                                    }
                                }

                                @Override
                                public void messagesRemoved(MessageCountEvent e) {
                                    try {
                                        wlMessage.acquire();
                                        Log.i(folder.name + " messages removed");

                                        for (Message imessage : e.getMessages()) {
                                            long uid = ifolder.getUID(imessage);
                                            EntityOperation.queue(ServiceSynchronize.this, folder, EntityOperation.FETCH, uid);
                                        }
                                    } catch (Throwable ex) {
                                        Log.e(folder.name, ex);
                                        EntityLog.log(
                                                ServiceSynchronize.this,
                                                folder.name + " " + Log.formatThrowable(ex, false));
                                        state.error(ex);
                                    } finally {
                                        wlMessage.release();
                                    }
                                }
                            });

                            // Flags (like "seen") at the remote could be changed while synchronizing

                            // Listen for changed messages
                            ifolder.addMessageChangedListener(new MessageChangedListener() {
                                @Override
                                public void messageChanged(MessageChangedEvent e) {
                                    try {
                                        wlMessage.acquire();
                                        Log.i(folder.name + " message changed");

                                        long uid = ifolder.getUID(e.getMessage());
                                        EntityOperation.queue(ServiceSynchronize.this, folder, EntityOperation.FETCH, uid);
                                    } catch (Throwable ex) {
                                        Log.e(folder.name, ex);
                                        EntityLog.log(
                                                ServiceSynchronize.this,
                                                folder.name + " " + Log.formatThrowable(ex, false));
                                        state.error(ex);
                                    } finally {
                                        wlMessage.release();
                                    }
                                }
                            });

                            // Idle folder
                            Thread idler = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Log.i(folder.name + " start idle");
                                        while (ifolder.isOpen() && state.isRunning() && state.isRecoverable()) {
                                            Log.i(folder.name + " do idle");
                                            ifolder.idle(false);
                                        }
                                    } catch (Throwable ex) {
                                        Log.e(folder.name, ex);
                                        EntityLog.log(
                                                ServiceSynchronize.this,
                                                folder.name + " " + Log.formatThrowable(ex, false));
                                        state.error(new FolderClosedException(ifolder, "IDLE"));
                                    } finally {
                                        Log.i(folder.name + " end idle");
                                    }
                                }
                            }, "idler." + folder.id);
                            idler.setPriority(THREAD_PRIORITY_BACKGROUND);
                            idler.start();
                            idlers.add(idler);

                            if (sync && folder.selectable)
                                EntityOperation.sync(this, folder.id, false);
                        } else
                            mapFolders.put(folder, null);

                        Log.d(folder.name + " observing");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                TwoStateOwner cowner = new TwoStateOwner(ServiceSynchronize.this, folder.name);
                                cowners.add(cowner);
                                cowner.start();

                                db.operation().liveOperations(folder.id).observe(cowner, new Observer<List<EntityOperation>>() {
                                    private List<Long> handling = new ArrayList<>();
                                    private final PowerManager.WakeLock wlFolder = pm.newWakeLock(
                                            PowerManager.PARTIAL_WAKE_LOCK, BuildConfig.APPLICATION_ID + ":folder." + folder.id);

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
                                            Log.i(folder.name + " operations=" + operations.size() +
                                                    " init=" + folder.initialize + " poll=" + folder.poll);

                                            executor.submit(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        wlFolder.acquire();
                                                        Log.i(folder.name + " process");

                                                        // Get folder
                                                        Folder ifolder = mapFolders.get(folder); // null when polling
                                                        boolean canOpen = (account.protocol == EntityAccount.TYPE_IMAP || EntityFolder.INBOX.equals(folder.type));
                                                        final boolean shouldClose = (ifolder == null && canOpen);

                                                        try {
                                                            Log.i(folder.name + " run " + (shouldClose ? "offline" : "online"));

                                                            if (shouldClose) {
                                                                // Prevent unnecessary folder connections
                                                                if (db.operation().getOperationCount(folder.id, null) == 0)
                                                                    return;

                                                                db.folder().setFolderState(folder.id, "connecting");

                                                                ifolder = iservice.getStore().getFolder(folder.name);
                                                                ifolder.open(Folder.READ_WRITE);

                                                                db.folder().setFolderState(folder.id, "connected");

                                                                db.folder().setFolderError(folder.id, null);
                                                            }

                                                            Core.processOperations(ServiceSynchronize.this,
                                                                    account, folder,
                                                                    iservice.getStore(), ifolder,
                                                                    state);

                                                        } catch (FolderNotFoundException ex) {
                                                            Log.w(folder.name, ex);
                                                            db.folder().deleteFolder(folder.id);
                                                        } catch (Throwable ex) {
                                                            Log.e(folder.name, ex);
                                                            EntityLog.log(
                                                                    ServiceSynchronize.this,
                                                                    folder.name + " " + Log.formatThrowable(ex, false));
                                                            db.folder().setFolderError(folder.id, Log.formatThrowable(ex));
                                                            state.error(ex);
                                                        } finally {
                                                            if (shouldClose) {
                                                                if (ifolder != null && ifolder.isOpen()) {
                                                                    db.folder().setFolderState(folder.id, "closing");
                                                                    try {
                                                                        ifolder.close(false);
                                                                    } catch (MessagingException ex) {
                                                                        Log.w(folder.name, ex);
                                                                    }
                                                                }
                                                                if (folder.synchronize && (folder.poll || !capIdle))
                                                                    db.folder().setFolderState(folder.id, "waiting");
                                                                else
                                                                    db.folder().setFolderState(folder.id, null);
                                                            }
                                                        }
                                                    } finally {
                                                        wlFolder.release();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }


                    // Keep alive
                    while (state.isRunning()) {
                        if (!state.isRecoverable())
                            throw new StoreClosedException(iservice.getStore(), "Unrecoverable");

                        // Sends store NOOP
                        if (!iservice.getStore().isConnected())
                            throw new StoreClosedException(iservice.getStore(), "NOOP");

                        if (sync)
                            for (EntityFolder folder : mapFolders.keySet())
                                if (folder.synchronize)
                                    if (!folder.poll && capIdle) {
                                        // Sends folder NOOP
                                        if (!mapFolders.get(folder).isOpen())
                                            throw new StoreClosedException(iservice.getStore(), folder.name);
                                    } else
                                        EntityOperation.sync(this, folder.id, false);

                        // Successfully connected: reset back off time
                        backoff = CONNECT_BACKOFF_START;

                        // Record successful connection
                        account.last_connected = new Date().getTime();
                        EntityLog.log(this, account.name + " set last_connected=" + new Date(account.last_connected));
                        db.account().setAccountConnected(account.id, account.last_connected);
                        db.account().setAccountWarning(account.id, capIdle ? null : getString(R.string.title_no_idle));

                        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        nm.cancel("receive:" + account.id, 1);

                        // Schedule keep alive alarm
                        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        try {
                            long duration = account.poll_interval * 60 * 1000L;
                            long trigger = System.currentTimeMillis() + duration;
                            EntityLog.log(this, "### " + account.name + " keep alive" +
                                    " wait=" + account.poll_interval + " until=" + new Date(trigger));
                            AlarmManagerCompat.setAndAllowWhileIdle(am, AlarmManager.RTC_WAKEUP, trigger, piWakeup);

                            try {
                                wlAccount.release();
                                state.acquire(2 * duration);
                            } catch (InterruptedException ex) {
                                EntityLog.log(this, account.name + " waited state=" + state);
                            } finally {
                                wlAccount.acquire();
                            }
                        } finally {
                            am.cancel(piWakeup);
                        }
                    }

                    Log.i(account.name + " done state=" + state);
                } catch (Throwable ex) {
                    Log.e(account.name, ex);
                    EntityLog.log(
                            ServiceSynchronize.this,
                            account.name + " " + Log.formatThrowable(ex, false));
                    db.account().setAccountError(account.id, Log.formatThrowable(ex));
                } finally {
                    // Update state
                    EntityLog.log(this, account.name + " closing");
                    db.account().setAccountState(account.id, "closing");

                    // Stop watching for operations
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (TwoStateOwner owner : cowners)
                                owner.destroy();
                        }
                    });

                    // Close folders
                    for (EntityFolder folder : mapFolders.keySet())
                        if (folder.synchronize && !folder.poll && mapFolders.get(folder) != null) {
                            db.folder().setFolderState(folder.id, "closing");
                            try {
                                if (iservice.getStore().isConnected())
                                    mapFolders.get(folder).close();
                            } catch (Throwable ex) {
                                Log.w(ex);
                            } finally {
                                db.folder().setFolderState(folder.id, null);
                            }
                        }

                    // Close store
                    try {
                        EntityLog.log(this, account.name + " store closing");
                        iservice.close();
                        EntityLog.log(this, account.name + " store closed");
                    } catch (Throwable ex) {
                        Log.w(account.name, ex);
                    } finally {
                        EntityLog.log(this, account.name + " closed");
                        db.account().setAccountState(account.id, null);
                    }

                    // Stop idlers
                    for (Thread idler : idlers)
                        state.join(idler);
                    idlers.clear();
                }

                if (state.isRunning()) {
                    if (backoff <= CONNECT_BACKOFF_MAX) {
                        // Short back-off period, keep device awake
                        EntityLog.log(this, account.name + " backoff=" + backoff);
                        try {
                            state.acquire(backoff * 1000L);
                        } catch (InterruptedException ex) {
                            Log.w(account.name + " backoff " + ex.toString());
                        }
                    } else {
                        // Long back-off period, let device sleep
                        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        try {
                            long duration = CONNECT_BACKOFF_AlARM * 60 * 1000L;
                            long trigger = System.currentTimeMillis() + duration;
                            EntityLog.log(this, "### " + account.name + " backoff" +
                                    " alarm=" + CONNECT_BACKOFF_AlARM + " until=" + new Date(trigger));
                            AlarmManagerCompat.setAndAllowWhileIdle(am, AlarmManager.RTC_WAKEUP, trigger, piWakeup);

                            try {
                                wlAccount.release();
                                state.acquire(2 * duration);
                            } catch (InterruptedException ex) {
                                Log.w(account.name + " backoff " + ex.toString());
                            } finally {
                                wlAccount.acquire();
                            }
                        } finally {
                            am.cancel(piWakeup);
                        }
                    }

                    if (backoff <= CONNECT_BACKOFF_MAX)
                        backoff *= 2;
                }
            }
        } finally {
            EntityLog.log(this, account.name + " stopped");
            wlAccount.release();
        }
    }

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            EntityLog.log(ServiceSynchronize.this, "Available network=" + network +
                    " capabilities " + cm.getNetworkCapabilities(network));
            updateState();
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities capabilities) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            EntityLog.log(ServiceSynchronize.this, "Changed network=" + network +
                    " capabilities " + cm.getNetworkCapabilities(network));
            updateState();
        }

        @Override
        public void onLost(@NonNull Network network) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo active = cm.getActiveNetworkInfo();
            EntityLog.log(ServiceSynchronize.this, "Lost network=" + network + " active=" + active);
            updateState();
        }

        private void updateState() {
            ConnectionHelper.NetworkState ns = ConnectionHelper.getNetworkState(ServiceSynchronize.this);
            liveNetworkState.postValue(ns);

            if (lastSuitable == null || lastSuitable != ns.isSuitable()) {
                lastSuitable = ns.isSuitable();
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(Helper.NOTIFICATION_SYNCHRONIZE, getNotificationService(lastAccounts, lastOperations).build());
            }
        }
    };

    private BroadcastReceiver connectionChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            EntityLog.log(ServiceSynchronize.this, "Received intent=" + intent +
                    " " + TextUtils.join(" ", Log.getExtras(intent.getExtras())));

            if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())) {
                boolean on = intent.getBooleanExtra("state", false);
                if (!on)
                    ;
            }

            networkCallback.onCapabilitiesChanged(null, null);
        }
    };

    private BroadcastReceiver onScreenOff = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Received " + intent);
            Log.logExtras(intent);
            Helper.clearAuthentication(ServiceSynchronize.this);
        }
    };

    static void boot(final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DB db = DB.getInstance(context);

                    // Restore notifications
                    db.message().clearNotifyingMessages();

                    // Restore snooze timers
                    for (EntityMessage message : db.message().getSnoozed())
                        EntityMessage.snooze(context, message.id, message.ui_snoozed);

                    // Restore schedule
                    schedule(context);

                    // Conditionally init service
                    int accounts = db.account().getSynchronizingAccounts().size();
                    if (accounts > 0)
                        eval(context, "boot");
                    else {
                        for (EntityAccount account : db.account().getAccounts())
                            db.account().setAccountState(account.id, null);

                        for (EntityFolder folder : db.folder().getFolders()) {
                            db.folder().setFolderState(folder.id, null);
                            db.folder().setFolderSyncState(folder.id, null);
                        }
                    }
                } catch (Throwable ex) {
                    Log.e(ex);
                }
            }
        }, "synchronize:boot");
        thread.setPriority(THREAD_PRIORITY_BACKGROUND);
        thread.start();
    }

    private static void schedule(Context context) {
        Intent alarm = new Intent(context, ServiceSynchronize.class);
        alarm.setAction("alarm");
        PendingIntent piAlarm;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            piAlarm = PendingIntent.getService(context, PI_ALARM, alarm, PendingIntent.FLAG_UPDATE_CURRENT);
        else
            piAlarm = PendingIntent.getForegroundService(context, PI_ALARM, alarm, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(piAlarm);

        long[] schedule = getSchedule(context);
        if (schedule == null)
            return;

        long now = new Date().getTime();
        long next = (now < schedule[0] ? schedule[0] : schedule[1]);
        boolean enabled = (now >= schedule[0] && now < schedule[1]);

        Log.i("Schedule now=" + new Date(now));
        Log.i("Schedule start=" + new Date(schedule[0]));
        Log.i("Schedule end=" + new Date(schedule[1]));
        Log.i("Schedule next=" + new Date(next));
        Log.i("Schedule enabled=" + enabled);

        AlarmManagerCompat.setAndAllowWhileIdle(am, AlarmManager.RTC_WAKEUP, next, piAlarm);

        WorkerPoll.init(context);
    }

    private static long[] getSchedule(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!prefs.getBoolean("schedule", false) || !ActivityBilling.isPro(context))
            return null;

        int minuteStart = prefs.getInt("schedule_start", 0);
        int minuteEnd = prefs.getInt("schedule_end", 0);

        if (minuteEnd <= minuteStart)
            minuteEnd += 24 * 60;

        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, minuteStart / 60);
        calStart.set(Calendar.MINUTE, minuteStart % 60);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, minuteEnd / 60);
        calEnd.set(Calendar.MINUTE, minuteEnd % 60);
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.MILLISECOND, 0);

        long now = new Date().getTime();
        if (now > calEnd.getTimeInMillis()) {
            calStart.set(Calendar.DAY_OF_MONTH, calStart.get(Calendar.DAY_OF_MONTH) + 1);
            calEnd.set(Calendar.DAY_OF_MONTH, calEnd.get(Calendar.DAY_OF_MONTH) + 1);
        }
        if (now > calEnd.getTimeInMillis())
            Log.e("Schedule invalid now=" + new Date(now) + " end=" + new Date(calEnd.getTimeInMillis()));

        long start = calStart.getTimeInMillis();
        long end = calEnd.getTimeInMillis();
        if (start > end)
            Log.e("Schedule invalid start=" + new Date(start) + " end=" + new Date(end));

        return new long[]{start, end};
    }

    static void eval(Context context, String reason) {
        ContextCompat.startForegroundService(context,
                new Intent(context, ServiceSynchronize.class)
                        .setAction("eval")
                        .putExtra("reason", reason));
    }

    static void reload(Context context, Long account, String reason) {
        ContextCompat.startForegroundService(context,
                new Intent(context, ServiceSynchronize.class)
                        .setAction("reload")
                        .putExtra("account", account == null ? -1 : account)
                        .putExtra("reason", reason));
    }

    static void reschedule(Context context) {
        ContextCompat.startForegroundService(context,
                new Intent(context, ServiceSynchronize.class)
                        .setAction("alarm"));
    }
}
