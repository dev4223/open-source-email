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

    Copyright 2018-2020 by Marcel Bokhorst (M66B)
*/

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;
import androidx.preference.PreferenceManager;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentOptionsSynchronize extends FragmentBase implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SwitchCompat swEnabled;
    private Spinner spPollInterval;
    private SwitchCompat swSchedule;
    private TextView tvSchedulePro;
    private TextView tvScheduleStart;
    private TextView tvScheduleEnd;
    private CheckBox[] cbDay;
    private SwitchCompat swUnseen;
    private SwitchCompat swFlagged;
    private SwitchCompat swDeleteUnseen;
    private SwitchCompat swSyncKept;
    private SwitchCompat swSyncFolders;
    private SwitchCompat swSubscriptions;
    private TextView tvSubscriptionPro;
    private SwitchCompat swSubscribedOnly;
    private SwitchCompat swCheckMx;
    private SwitchCompat swCheckReply;

    private final static String[] RESET_OPTIONS = new String[]{
            "enabled", "poll_interval", "schedule", "schedule_start", "schedule_end",
            "sync_unseen", "sync_flagged", "delete_unseen", "sync_kept", "sync_folders", "subscriptions", "subscribed_only",
            "check_mx", "check_reply"
    };

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setSubtitle(R.string.title_setup);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_options_synchronize, container, false);

        // Get controls

        swEnabled = view.findViewById(R.id.swEnabled);
        spPollInterval = view.findViewById(R.id.spPollInterval);
        swSchedule = view.findViewById(R.id.swSchedule);
        tvSchedulePro = view.findViewById(R.id.tvSchedulePro);
        tvScheduleStart = view.findViewById(R.id.tvScheduleStart);
        tvScheduleEnd = view.findViewById(R.id.tvScheduleEnd);
        cbDay = new CheckBox[]{
                view.findViewById(R.id.cbDay0),
                view.findViewById(R.id.cbDay1),
                view.findViewById(R.id.cbDay2),
                view.findViewById(R.id.cbDay3),
                view.findViewById(R.id.cbDay4),
                view.findViewById(R.id.cbDay5),
                view.findViewById(R.id.cbDay6)
        };
        swUnseen = view.findViewById(R.id.swUnseen);
        swFlagged = view.findViewById(R.id.swFlagged);
        swDeleteUnseen = view.findViewById(R.id.swDeleteUnseen);
        swSyncKept = view.findViewById(R.id.swSyncKept);
        swSyncFolders = view.findViewById(R.id.swSyncFolders);
        swSubscriptions = view.findViewById(R.id.swSubscriptions);
        tvSubscriptionPro = view.findViewById(R.id.tvSubscriptionPro);
        swSubscribedOnly = view.findViewById(R.id.swSubscribedOnly);
        swCheckMx = view.findViewById(R.id.swCheckMx);
        swCheckReply = view.findViewById(R.id.swCheckReply);

        setOptions();

        // Wire controls

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        swEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("enabled", checked).apply();
                ServiceSynchronize.eval(getContext(), "enabled=" + checked);
            }
        });

        spPollInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object tag = adapterView.getTag();
                int current = (tag == null ? 0 : (Integer) tag);
                int[] values = getResources().getIntArray(R.array.pollIntervalValues);
                int value = values[position];
                if (value != current) {
                    adapterView.setTag(value);
                    prefs.edit().putInt("poll_interval", value).apply();
                    ServiceSynchronize.reschedule(getContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setTag(null);
                prefs.edit().remove("poll_interval").apply();
                ServiceSynchronize.reschedule(getContext());
            }
        });

        swSchedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("schedule", checked).apply();
                ServiceSynchronize.reschedule(getContext());
            }
        });

        Helper.linkPro(tvSchedulePro);

        tvScheduleStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putBoolean("start", true);
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.setArguments(args);
                timePicker.show(getParentFragmentManager(), "timePicker");
            }
        });

        tvScheduleEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putBoolean("start", false);
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.setArguments(args);
                timePicker.show(getParentFragmentManager(), "timePicker");
            }
        });

        String[] daynames = new DateFormatSymbols().getWeekdays();
        for (int i = 0; i < 7; i++) {
            final int day = i;
            cbDay[i].setText(daynames[i + 1]);
            cbDay[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    prefs.edit().putBoolean("schedule_day" + day, isChecked).apply();
                    ServiceSynchronize.reschedule(getContext());
                }
            });
        }

        swUnseen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("sync_unseen", checked).apply();
                ServiceSynchronize.reload(getContext(), null, "sync_unseen=" + checked);
            }
        });

        swFlagged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("sync_flagged", checked).apply();
                ServiceSynchronize.reload(getContext(), null, "sync_flagged=" + checked);
            }
        });

        swDeleteUnseen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("delete_unseen", checked).apply();
                ServiceSynchronize.reload(getContext(), null, "delete_unseen=" + checked);
            }
        });

        swSyncKept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("sync_kept", checked).apply();
                ServiceSynchronize.reload(getContext(), null, "sync_kept=" + checked);
            }
        });

        swSyncFolders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("sync_folders", checked).apply();
                ServiceSynchronize.reload(getContext(), null, "sync_folders=" + checked);
            }
        });

        swSubscriptions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("subscriptions", checked).apply();
            }
        });

        Helper.linkPro(tvSubscriptionPro);

        swSubscribedOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("subscribed_only", checked).apply();
                ServiceSynchronize.reload(getContext(), null, "subscribed_only");
            }
        });

        swCheckMx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("check_mx", checked).apply();
            }
        });

        swCheckReply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("check_reply", checked).apply();
            }
        });

        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            setOptions();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_default:
                onMenuDefault();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onMenuDefault() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        for (String option : RESET_OPTIONS)
            editor.remove(option);
        editor.apply();
        ToastEx.makeText(getContext(), R.string.title_setup_done, Toast.LENGTH_LONG).show();
    }

    private void setOptions() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean pro = ActivityBilling.isPro(getContext());

        swEnabled.setChecked(prefs.getBoolean("enabled", true));

        int pollInterval = prefs.getInt("poll_interval", 0);
        int[] pollIntervalValues = getResources().getIntArray(R.array.pollIntervalValues);
        for (int pos = 0; pos < pollIntervalValues.length; pos++)
            if (pollIntervalValues[pos] == pollInterval) {
                spPollInterval.setTag(pollInterval);
                spPollInterval.setSelection(pos);
                break;
            }

        swSchedule.setChecked(prefs.getBoolean("schedule", false) && pro);
        swSchedule.setEnabled(pro);
        tvScheduleStart.setText(formatHour(getContext(), prefs.getInt("schedule_start", 0)));
        tvScheduleEnd.setText(formatHour(getContext(), prefs.getInt("schedule_end", 0)));
        for (int i = 0; i < 7; i++)
            cbDay[i].setChecked(prefs.getBoolean("schedule_day" + i, true));

        swUnseen.setChecked(prefs.getBoolean("sync_unseen", false));
        swFlagged.setChecked(prefs.getBoolean("sync_flagged", false));
        swDeleteUnseen.setChecked(prefs.getBoolean("delete_unseen", false));
        swSyncKept.setChecked(prefs.getBoolean("sync_kept", true));
        swSyncFolders.setChecked(prefs.getBoolean("sync_folders", true));
        swSubscriptions.setChecked(prefs.getBoolean("subscriptions", false) && pro);
        swSubscriptions.setEnabled(pro);
        swSubscribedOnly.setChecked(prefs.getBoolean("subscribed_only", false));
        swCheckMx.setChecked(prefs.getBoolean("check_mx", false));
        swCheckReply.setChecked(prefs.getBoolean("check_reply", false));
    }

    private String formatHour(Context context, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, minutes / 60);
        cal.set(Calendar.MINUTE, minutes % 60);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return Helper.getTimeInstance(context, SimpleDateFormat.SHORT).format(cal.getTime());
    }

    public static class TimePickerFragment extends FragmentDialogBase implements TimePickerDialog.OnTimeSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments();
            boolean start = args.getBoolean("start");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            int minutes = prefs.getInt("schedule_" + (start ? "start" : "end"), 0);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, minutes / 60);
            cal.set(Calendar.MINUTE, minutes % 60);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            return new TimePickerDialog(getContext(), this,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(getContext()));
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
            Bundle args = getArguments();
            boolean start = args.getBoolean("start");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("schedule_" + (start ? "start" : "end"), hour * 60 + minute);
            editor.putBoolean("schedule", true);
            editor.apply();

            ServiceSynchronize.reschedule(getContext());
        }
    }
}
