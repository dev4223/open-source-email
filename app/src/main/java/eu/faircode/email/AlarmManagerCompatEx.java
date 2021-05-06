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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.AlarmManagerCompat;
import androidx.preference.PreferenceManager;

public class AlarmManagerCompatEx {
    static void setAndAllowWhileIdle(
            @NonNull Context context, @NonNull AlarmManager am,
            int type, long trigger, @NonNull PendingIntent pi) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean exact_alarms = prefs.getBoolean("exact_alarms", false);

        if (exact_alarms)
            AlarmManagerCompat.setExactAndAllowWhileIdle(am, type, trigger, pi);
        else
            AlarmManagerCompat.setAndAllowWhileIdle(am, type, trigger, pi);
    }
}
