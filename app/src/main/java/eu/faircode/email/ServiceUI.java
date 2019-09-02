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

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.RemoteInput;
import androidx.preference.PreferenceManager;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

public class ServiceUI extends IntentService {
    static final int PI_CLEAR = 1;
    static final int PI_TRASH = 2;
    static final int PI_ARCHIVE = 3;
    static final int PI_REPLY_DIRECT = 4;
    static final int PI_FLAG = 5;
    static final int PI_SEEN = 6;
    static final int PI_IGNORED = 7;
    static final int PI_SNOOZED = 8;

    public ServiceUI() {
        this(ServiceUI.class.getName());
    }

    public ServiceUI(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        Log.i("Service UI create");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i("Service UI destroy");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Under certain circumstances, a background app is placed on a temporary whitelist for several minutes
        // - Executing a PendingIntent from a notification.
        // https://developer.android.com/about/versions/oreo/background#services
        Log.i("Service UI intent=" + intent);
        Log.logExtras(intent);

        if (intent == null)
            return;

        String action = intent.getAction();
        if (action == null)
            return;

        try {
            String[] parts = action.split(":");
            long id = (parts.length > 1 ? Long.parseLong(parts[1]) : -1);
            String group = intent.getStringExtra("group");

            switch (parts[0]) {
                case "clear":
                    onClear(id);
                    break;

                case "trash":
                    cancel(group, id);
                    onTrash(id);
                    break;

                case "archive":
                    cancel(group, id);
                    onArchive(id);
                    break;

                case "reply":
                    onReplyDirect(id, intent);
                    cancel(group, id);
                    onSeen(id);
                    break;

                case "flag":
                    cancel(group, id);
                    onFlag(id);
                    break;

                case "seen":
                    cancel(group, id);
                    onSeen(id);
                    break;

                case "ignore":
                    onIgnore(id);
                    break;

                case "snooze":
                    // AlarmManager.RTC_WAKEUP
                    // When the alarm is dispatched, the app will also be added to the system's temporary whitelist
                    // for approximately 10 seconds to allow that application to acquire further wake locks in which to complete its work.
                    // https://developer.android.com/reference/android/app/AlarmManager
                    onSnooze(id);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown UI action: " + parts[0]);
            }
        } catch (Throwable ex) {
            Log.e(ex);
        }
    }

    private void onClear(long group) {
        DB db = DB.getInstance(this);
        int cleared = db.message().ignoreAll(group == 0 ? null : group);
        Log.i("Cleared=" + cleared);
    }

    private void cancel(String group, long id) {
        String tag = "unseen." + group + ":" + id;

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(tag, 1);
    }

    private void onTrash(long id) {
        DB db = DB.getInstance(this);
        try {
            db.beginTransaction();

            EntityMessage message = db.message().getMessage(id);
            if (message != null) {
                EntityFolder trash = db.folder().getFolderByType(message.account, EntityFolder.TRASH);
                if (trash != null)
                    EntityOperation.queue(this, message, EntityOperation.MOVE, trash.id);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void onArchive(long id) {
        DB db = DB.getInstance(this);
        try {
            db.beginTransaction();

            EntityMessage message = db.message().getMessage(id);
            if (message != null) {
                EntityFolder archive = db.folder().getFolderByType(message.account, EntityFolder.ARCHIVE);
                if (archive == null)
                    archive = db.folder().getFolderByType(message.account, EntityFolder.TRASH);
                if (archive != null)
                    EntityOperation.queue(this, message, EntityOperation.MOVE, archive.id);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void onReplyDirect(long id, Intent intent) throws IOException {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean prefix_once = prefs.getBoolean("prefix_once", true);
        boolean plain_only = prefs.getBoolean("plain_only", false);

        Bundle results = RemoteInput.getResultsFromIntent(intent);
        String text = results.getString("text");
        if (text != null)
            text = "<p>" + text.replaceAll("\\r?\\n", "<br>") + "</p>";

        DB db = DB.getInstance(this);
        try {
            db.beginTransaction();

            EntityMessage ref = db.message().getMessage(id);
            if (ref == null)
                throw new IllegalArgumentException("message not found");

            EntityIdentity identity = db.identity().getIdentity(ref.identity);
            if (identity == null)
                throw new IllegalArgumentException("identity not found");

            EntityFolder outbox = db.folder().getOutbox();
            if (outbox == null)
                throw new IllegalArgumentException("outbox not found");

            String subject = (ref.subject == null ? "" : ref.subject);
            if (prefix_once) {
                String re = getString(R.string.title_subject_reply, "");
                subject = subject.replaceAll("(?i)" + Pattern.quote(re.trim()), "").trim();
            }

            EntityMessage reply = new EntityMessage();
            reply.account = identity.account;
            reply.folder = outbox.id;
            reply.identity = identity.id;
            reply.msgid = EntityMessage.generateMessageId();
            reply.inreplyto = ref.msgid;
            reply.thread = ref.thread;
            reply.to = ref.from;
            reply.from = new Address[]{new InternetAddress(identity.email, identity.name)};
            reply.subject = getString(R.string.title_subject_reply, subject);
            reply.received = new Date().getTime();
            reply.seen = true;
            reply.ui_seen = true;
            reply.id = db.message().insertMessage(reply);
            Helper.writeText(reply.getFile(this), text);
            db.message().setMessageContent(reply.id,
                    true,
                    plain_only || ref.plain_only,
                    HtmlHelper.getPreview(text),
                    null);

            EntityOperation.queue(this, reply, EntityOperation.SEND);

            db.setTransactionSuccessful();

            ToastEx.makeText(this, R.string.title_queued, Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
    }

    private void onFlag(long id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean threading = prefs.getBoolean("threading", true);

        DB db = DB.getInstance(this);
        try {
            db.beginTransaction();

            EntityMessage message = db.message().getMessage(id);
            if (message != null) {
                List<EntityMessage> messages = db.message().getMessagesByThread(
                        message.account, message.thread, threading ? null : id, null);
                for (EntityMessage threaded : messages) {
                    EntityOperation.queue(this, threaded, EntityOperation.FLAG, true);
                    EntityOperation.queue(this, threaded, EntityOperation.SEEN, true);
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void onSeen(long id) {
        DB db = DB.getInstance(this);
        try {
            db.beginTransaction();

            EntityMessage message = db.message().getMessage(id);
            if (message != null)
                EntityOperation.queue(this, message, EntityOperation.SEEN, true);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void onIgnore(long id) {
        DB db = DB.getInstance(this);
        try {
            db.beginTransaction();

            EntityMessage message = db.message().getMessage(id);
            if (message != null)
                db.message().setMessageUiIgnored(message.id, true);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void onSnooze(long id) {
        DB db = DB.getInstance(this);
        try {
            db.beginTransaction();

            EntityMessage message = db.message().getMessage(id);
            if (message != null) {
                db.message().setMessageSnoozed(message.id, null);

                EntityFolder folder = db.folder().getFolder(message.folder);
                if (EntityFolder.OUTBOX.equals(folder.type)) {
                    Log.i("Delayed send id=" + message.id);
                    EntityOperation.queue(this, message, EntityOperation.SEND);
                } else {
                    if (folder.notify)
                        EntityOperation.queue(this, message, EntityOperation.SEEN, false, false);
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
