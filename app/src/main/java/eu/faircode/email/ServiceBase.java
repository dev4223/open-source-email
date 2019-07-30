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

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.LifecycleService;

import com.bugsnag.android.BreadcrumbType;
import com.bugsnag.android.Bugsnag;

import java.util.HashMap;
import java.util.Map;

abstract class ServiceBase extends LifecycleService {
    @Override
    public void onCreate() {
        Map<String, String> crumb = new HashMap<>();
        crumb.put("state", "create");
        Bugsnag.leaveBreadcrumb(this.getClass().getSimpleName(), BreadcrumbType.STATE, crumb);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Map<String, String> crumb = new HashMap<>();
        if (intent != null) {
            crumb.put("action", intent.getAction());
            Bundle data = intent.getExtras();
            if (data != null)
                for (String key : data.keySet()) {
                    Object value = data.get(key);
                    crumb.put(key, value == null ? null : value.toString());
                }
        }
        Bugsnag.leaveBreadcrumb(this.getClass().getSimpleName(), BreadcrumbType.LOG, crumb);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Map<String, String> crumb = new HashMap<>();
        crumb.put("state", "destroy");
        Bugsnag.leaveBreadcrumb(this.getClass().getSimpleName(), BreadcrumbType.STATE, crumb);

        super.onDestroy();
    }
}
