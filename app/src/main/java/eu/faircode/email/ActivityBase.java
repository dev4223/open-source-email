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

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class ActivityBase extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Context originalContext;
    private boolean contacts;
    private List<IBackPressedListener> backPressedListeners = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context base) {
        originalContext = base;
        super.attachBaseContext(ApplicationEx.getLocalizedContext(base));
    }

    Context getOriginalContext() {
        return originalContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Create " + this.getClass().getName() + " version=" + BuildConfig.VERSION_NAME);
        Intent intent = getIntent();
        if (intent != null) {
            Log.i(intent.toString());
            Log.logBundle(intent.getExtras());
        }

        this.contacts = hasPermission(Manifest.permission.READ_CONTACTS);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean secure = prefs.getBoolean("secure", false);
        if (secure)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        if (!this.getClass().equals(ActivityMain.class)) {
            String theme = prefs.getString("theme", "light");
            int uiMode = getResources().getConfiguration().uiMode;
            boolean night = (uiMode & Configuration.UI_MODE_NIGHT_YES) != 0;
            Log.i("theme=" + theme + " UI mode=" + uiMode + " night=" + night);

            switch (theme) {
                case "light":
                case "blue_orange_light":
                    setTheme(R.style.AppThemeBlueOrangeLight);
                    break;
                case "orange_blue_light":
                    setTheme(R.style.AppThemeOrangeBlueLight);
                    break;

                case "yellow_purple_light":
                    setTheme(R.style.AppThemeYellowPurpleLight);
                    break;
                case "purple_yellow_light":
                    setTheme(R.style.AppThemePurpleYellowLight);
                    break;

                case "red_green_light":
                    setTheme(R.style.AppThemeRedGreenLight);
                    break;
                case "green_red_light":
                    setTheme(R.style.AppThemeGreenRedLight);
                    break;

                case "dark":
                case "blue_orange_dark":
                    setTheme(R.style.AppThemeBlueOrangeDark);
                    break;
                case "orange_blue_dark":
                    setTheme(R.style.AppThemeOrangeBlueDark);
                    break;

                case "yellow_purple_dark":
                    setTheme(R.style.AppThemeYellowPurpleDark);
                    break;
                case "purple_yellow_dark":
                    setTheme(R.style.AppThemePurpleYellowDark);
                    break;

                case "red_green_dark":
                    setTheme(R.style.AppThemeRedGreenDark);
                    break;
                case "green_red_dark":
                    setTheme(R.style.AppThemeGreenRedDark);
                    break;

                case "grey_light":
                    setTheme(R.style.AppThemeGreySteelBlueLight);
                    break;
                case "grey_dark":
                    setTheme(R.style.AppThemeGreySteelBlueDark);
                    break;
                case "black":
                    setTheme(R.style.AppThemeBlack);
                    break;

                case "system":
                case "blue_orange_system":
                    setTheme(night
                            ? R.style.AppThemeBlueOrangeDark : R.style.AppThemeBlueOrangeLight);
                    break;
                case "yellow_purple_system":
                    setTheme(night
                            ? R.style.AppThemeYellowPurpleDark : R.style.AppThemeYellowPurpleLight);
                    break;
                case "red_green_system":
                    setTheme(night
                            ? R.style.AppThemeRedGreenDark : R.style.AppThemeRedGreenLight);
                    break;
                case "grey_system":
                    setTheme(night
                            ? R.style.AppThemeGreySteelBlueDark : R.style.AppThemeGreySteelBlueLight);
                    break;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean dark = Helper.isDarkTheme(this);
                Window window = getWindow();
                View view = window.getDecorView();
                int flags = view.getSystemUiVisibility();
                if (dark)
                    flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                view.setSystemUiVisibility(flags);
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(this);

        checkAuthentication();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int before = Helper.getSize(outState);
        super.onSaveInstanceState(outState);
        int after = Helper.getSize(outState);
        Log.d("Saved instance " + this + " size=" + before + "/" + after);

        Map<String, String> crumb = new HashMap<>();
        crumb.put("name", this.getClass().getName());
        crumb.put("before", Integer.toString(before));
        crumb.put("after", Integer.toString(after));
        Log.breadcrumb("onSaveInstanceState", crumb);

        for (String key : outState.keySet())
            Log.d("Saved " + this + " " + key + "=" + outState.get(key));
    }

    @Override
    protected void onResume() {
        Log.d("Resume " + this.getClass().getName());

        boolean contacts = hasPermission(Manifest.permission.READ_CONTACTS);
        if (!this.getClass().equals(ActivitySetup.class) && this.contacts != contacts) {
            Log.i("Contacts permission=" + contacts);
            finish();
            startActivity(getIntent());
        } else
            checkAuthentication();

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("Pause " + this.getClass().getName());
        super.onPause();

        if (!this.getClass().equals(ActivityMain.class) && Helper.shouldAuthenticate(this))
            finishAndRemoveTask();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("Config " + this.getClass().getName());
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onUserInteraction() {
        Log.d("User interaction");

        if (!this.getClass().equals(ActivityMain.class) && Helper.shouldAuthenticate(this)) {
            finishAndRemoveTask();
            Intent main = new Intent(this, ActivityMain.class);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(main);
        }
    }

    @Override
    protected void onUserLeaveHint() {
        Log.d("User leaving");
    }

    @Override
    protected void onStop() {
        super.onStop();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm != null && !pm.isInteractive()) {
            Log.i("Stop with screen off");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean biometrics = prefs.getBoolean("biometrics", false);
            if (biometrics) {
                Helper.clearAuthentication(this);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("Destroy " + this.getClass().getName());
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String action = (data == null ? null : data.getAction());
        Log.i("Result class=" + this.getClass().getSimpleName() +
                " action=" + action + " request=" + requestCode + " result=" + resultCode);
        Log.logExtras(data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkAuthentication() {
        if (!this.getClass().equals(ActivityMain.class) && Helper.shouldAuthenticate(this)) {
            Intent intent = getIntent();
            finishAndRemoveTask();
            Intent main = new Intent(this, ActivityMain.class)
                    .putExtra("intent", intent);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(main);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Log.e(ex);
            ToastEx.makeText(this, getString(R.string.title_no_viewer, intent.getAction()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException ex) {
            Log.e(ex);
            ToastEx.makeText(this, getString(R.string.title_no_viewer, intent.getAction()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        Log.i("Preference " + key + "=" + prefs.getAll().get(key));
        if ("theme".equals(key)) {
            finish();
            if (this.getClass().equals(ActivitySetup.class))
                startActivity(getIntent());
        } else if (!this.getClass().equals(ActivitySetup.class) &&
                Arrays.asList(FragmentOptions.OPTIONS_RESTART).contains(key))
            finish();
    }

    public boolean hasPermission(String name) {
        return Helper.hasPermission(this, name);
    }

    void addBackPressedListener(final IBackPressedListener listener, LifecycleOwner owner) {
        Log.d("Adding back listener=" + listener);
        backPressedListeners.add(listener);

        owner.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            public void onDestroyed() {
                Log.d("Removing back listener=" + listener);
                backPressedListeners.remove(listener);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backHandled())
            return;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
                    onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected boolean backHandled() {
        for (IBackPressedListener listener : backPressedListeners)
            if (listener.onBackPressed())
                return true;
        return false;
    }

    public interface IBackPressedListener {
        boolean onBackPressed();
    }
}
