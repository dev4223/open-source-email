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

    Copyright 2018-2021 by Marcel Bokhorst (M66B)
*/

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.util.Printer;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplicationEx extends Application
        implements androidx.work.Configuration.Provider, SharedPreferences.OnSharedPreferenceChangeListener {
    private Thread.UncaughtExceptionHandler prev = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(getLocalizedContext(base));
    }

    static Context getLocalizedContext(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (prefs.contains("english")) {
            boolean english = prefs.getBoolean("english", false);
            if (english)
                prefs.edit()
                        .remove("english")
                        .putString("language", Locale.US.toLanguageTag())
                        .commit(); // apply won't work here
        }

        String language = prefs.getString("language", null);
        if (language != null) {
            Locale locale = Locale.forLanguageTag(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration(context.getResources().getConfiguration());
            config.setLocale(locale);
            return context.createConfigurationContext(config);
        }

        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        long start = new Date().getTime();
        Log.i("App create" +
                " version=" + BuildConfig.VERSION_NAME +
                " process=" + android.os.Process.myPid());
        Log.logMemory(this, "App");

        getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String msg) {
                if (BuildConfig.DEBUG)
                    Log.d("Loop: " + msg);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean crash_reports = prefs.getBoolean("crash_reports", false);

        prev = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                if (!crash_reports && Log.isOwnFault(ex)) {
                    Log.e(ex);

                    if (BuildConfig.BETA_RELEASE ||
                            !Helper.isPlayStoreInstall())
                        Log.writeCrashLog(ApplicationEx.this, ex);

                    if (prev != null)
                        prev.uncaughtException(thread, ex);
                } else {
                    Log.w(ex);
                    System.exit(1);
                }
            }
        });

        Log.setup(this);

        upgrade(this);

        try {
            boolean tcp_keep_alive = prefs.getBoolean("tcp_keep_alive", false);
            System.setProperty("fairemail.tcp_keep_alive", Boolean.toString(tcp_keep_alive));
        } catch (Throwable ex) {
            Log.e(ex);
        }

        prefs.registerOnSharedPreferenceChangeListener(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            NotificationHelper.createNotificationChannels(this);

        DB.setupViewInvalidation(this);

        if (Helper.hasWebView(this))
            CookieManager.getInstance().setAcceptCookie(false);

        MessageHelper.setSystemProperties(this);

        ContactInfo.init(this);

        DisconnectBlacklist.init(this);

        boolean watchdog = prefs.getBoolean("watchdog", true);
        boolean enabled = prefs.getBoolean("enabled", true);
        if (watchdog && enabled)
            WorkerWatchdog.init(this);
        else {
            ServiceSynchronize.watchdog(this);
            ServiceSend.watchdog(this);
        }

        WorkerCleanup.init(this);

        registerReceiver(onScreenOff, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        long end = new Date().getTime();
        Log.i("App created " + (end - start) + " ms");
    }

    @NonNull
    @Override
    public androidx.work.Configuration getWorkManagerConfiguration() {
        // https://developer.android.com/topic/libraries/architecture/workmanager/advanced/custom-configuration
        return new androidx.work.Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "enabled":
                ServiceSynchronize.reschedule(this);
                WorkerCleanup.init(this);
                WorkerWatchdog.init(this);
                break;
            case "poll_interval":
            case "schedule":
            case "schedule_start":
            case "schedule_end":
            case "schedule_day0":
            case "schedule_day1":
            case "schedule_day2":
            case "schedule_day3":
            case "schedule_day4":
            case "schedule_day5":
            case "schedule_day6":
                ServiceSynchronize.reschedule(this);
                break;
            case "watchdog":
                WorkerWatchdog.init(this);
                break;
            case "secure": // privacy
            case "shortcuts": // misc
            case "language": // misc
            case "query_threads": // misc
            case "wal": // misc
                // Should be excluded for import
                restart();
                break;
            case "debug":
            case "log_level":
                Log.setLevel(this);
                break;
        }
    }

    void restart() {
        Intent intent = new Intent(this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Runtime.getRuntime().exit(0);
    }

    @Override
    public void onTrimMemory(int level) {
        Log.logMemory(this, "Trim memory level=" + level);
        Map<String, String> crumb = new HashMap<>();
        crumb.put("level", Integer.toString(level));
        crumb.put("free", Integer.toString(Log.getFreeMemMb()));
        Log.breadcrumb("trim", crumb);
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        Log.logMemory(this, "Low memory");
        Map<String, String> crumb = new HashMap<>();
        crumb.put("free", Integer.toString(Log.getFreeMemMb()));
        Log.breadcrumb("low", crumb);

        ContactInfo.clearCache(this, false);

        super.onLowMemory();
    }

    static void upgrade(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int version = prefs.getInt("version", BuildConfig.VERSION_CODE);
        if (version != BuildConfig.VERSION_CODE)
            EntityLog.log(context, "Upgrading from " + version + " to " + BuildConfig.VERSION_CODE);

        SharedPreferences.Editor editor = prefs.edit();

        if (version < BuildConfig.VERSION_CODE)
            editor.remove("crash_report_count");

        if (version < 468) {
            editor.remove("notify_trash");
            editor.remove("notify_archive");
            editor.remove("notify_reply");
            editor.remove("notify_flag");
            editor.remove("notify_seen");

        } else if (version < 601) {
            editor.putBoolean("contact_images", prefs.getBoolean("autoimages", true));
            editor.remove("autoimages");

        } else if (version < 612) {
            if (prefs.getBoolean("autonext", false))
                editor.putString("onclose", "next");
            editor.remove("autonext");

        } else if (version < 693) {
            editor.remove("message_swipe");
            editor.remove("message_select");

        } else if (version < 696) {
            String theme = prefs.getString("theme", "light");
            if ("grey".equals(theme))
                editor.putString("theme", "grey_dark");

            if (prefs.contains("ascending")) {
                editor.putBoolean("ascending_list", prefs.getBoolean("ascending", false));
                editor.remove("ascending");
            }

        } else if (version < 701) {
            if (prefs.getBoolean("suggest_local", false)) {
                editor.putBoolean("suggest_sent", true);
                editor.remove("suggest_local");
            }

        } else if (version < 703) {
            if (!prefs.getBoolean("style_toolbar", true)) {
                editor.putBoolean("compose_media", false);
                editor.remove("style_toolbar");
            }

        } else if (version < 709) {
            if (prefs.getBoolean("swipe_reversed", false)) {
                editor.putBoolean("reversed", true);
                editor.remove("swipe_reversed");
            }

        } else if (version < 741)
            editor.remove("send_dialog");

        else if (version < 751) {
            if (prefs.contains("notify_snooze_duration")) {
                int minutes = prefs.getInt("notify_snooze_duration", 60);
                int hours = (int) Math.ceil(minutes / 60.0);
                editor.putInt("default_snooze", hours);
                editor.remove("notify_snooze_duration");
            }

        } else if (version < 819) {
            if (prefs.contains("no_history")) {
                editor.putBoolean("secure", prefs.getBoolean("no_history", false));
                editor.remove("no_history");
            }

            if (prefs.contains("zoom")) {
                int zoom = prefs.getInt("zoom", 1);
                editor.putInt("view_zoom", zoom);
                editor.putInt("compose_zoom", zoom);
                editor.remove("zoom");
            }

        } else if (version < 844) {
            if (prefs.getBoolean("schedule", false))
                editor.putBoolean("enabled", true);

        } else if (version < 874) {
            if (prefs.contains("experiments") &&
                    prefs.getBoolean("experiments", false))
                editor.putBoolean("quick_filter", true);
            editor.remove("experiments");

        } else if (version < 889) {
            if (prefs.contains("autoresize")) {
                boolean autoresize = prefs.getBoolean("autoresize", true);
                editor.putBoolean("resize_images", autoresize);
                editor.putBoolean("resize_attachments", autoresize);
                editor.remove("autoresize");
            }
        } else if (version < 930) {
            boolean large = context.getResources().getConfiguration()
                    .isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE);
            editor.putBoolean("landscape3", large);
        } else if (version < 949) {
            if (prefs.contains("automove")) {
                boolean automove = prefs.getBoolean("automove", false);
                editor.putBoolean("move_1_confirmed", automove);
                editor.remove("automove");
            }
        } else if (version < 972) {
            if (prefs.contains("signature_end")) {
                boolean signature_end = prefs.getBoolean("signature_end", false);
                if (signature_end)
                    editor.putInt("signature_location", 2);
                editor.remove("signature_end");
            }
        } else if (version < 978) {
            if (!prefs.contains("poll_interval"))
                editor.putInt("poll_interval", 0);
            editor.remove("first");
        } else if (version < 1021) {
            boolean highlight_unread = prefs.getBoolean("highlight_unread", false);
            if (!highlight_unread)
                editor.putBoolean("highlight_unread", highlight_unread);
        } else if (version < 1121) {
            if (!Helper.isPlayStoreInstall())
                editor.putBoolean("experiments", true);
        } else if (version < 1124) {
            editor.remove("experiments");
        } else if (version < 1181) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                editor.remove("background_service");
        } else if (version < 1195)
            editor.remove("auto_optimize");
        else if (version < 1229) {
            boolean monospaced = prefs.getBoolean("monospaced", false);
            if (monospaced && !BuildConfig.DEBUG)
                editor.putBoolean("text_font", false);
        } else if (version < 1238) {
            if (!prefs.contains("subject_ellipsize"))
                editor.putString("subject_ellipsize", "middle");
            if (!prefs.contains("auto_optimize"))
                editor.putBoolean("auto_optimize", false);
        } else if (version < 1253) {
            int threads = prefs.getInt("query_threads", 4);
            if (threads == 4)
                editor.remove("query_threads");
        } else if (version < 1264) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N ||
                    "Blackview".equalsIgnoreCase(Build.MANUFACTURER) ||
                    "OnePlus".equalsIgnoreCase(Build.MANUFACTURER) ||
                    "HUAWEI".equalsIgnoreCase(Build.MANUFACTURER))
                editor.putInt("query_threads", 2);
        } else if (version < 1274)
            ContactInfo.clearCache(context); // Favicon background
        else if (version < 1336) {
            if (!prefs.contains("beige"))
                editor.putBoolean("beige", false);
        } else if (version < 1385)
            editor.remove("parse_classes");
        else if (version < 1401)
            editor.remove("tcp_keep_alive");
        else if (version < 1407)
            editor.remove("print_html_confirmed");
        else if (version < 1413)
            editor.remove("experiments");
        else if (version < 1439) {
            if (!BuildConfig.DEBUG)
                editor.remove("experiments");
        } else if (version < 1461) {
            if (!prefs.contains("theme"))
                editor.putString("theme", "blue_orange_light");
        } else if (version < 1463) {
            if (!prefs.contains("autoscroll"))
                editor.putBoolean("autoscroll", true);
        } else if (version < 1477) {
            if (!BuildConfig.DEBUG)
                editor.remove("experiments");
        } else if (version == 1492)
            editor.putBoolean("experiments", !BuildConfig.PLAY_STORE_RELEASE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !BuildConfig.DEBUG)
            editor.remove("background_service");

        if (version < BuildConfig.VERSION_CODE)
            editor.putInt("previous_version", version);
        editor.putInt("version", BuildConfig.VERSION_CODE);

        editor.apply();
    }

    private BroadcastReceiver onScreenOff = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Received " + intent);
            Log.logExtras(intent);
            Helper.clearAuthentication(ApplicationEx.this);
        }
    };

    private static Handler handler = null;

    synchronized static Handler getMainHandler() {
        if (handler == null)
            handler = new Handler(Looper.getMainLooper());
        return handler;
    }
}
