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

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkerPoll extends Worker {
    public WorkerPoll(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.i("Instance " + getName());
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("Running " + getName());

        sync(getApplicationContext(), null);

        return Result.success();
    }

    static void init(Context context, boolean enabled) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            int pollInterval = prefs.getInt("poll_interval", 0);
            if (enabled && pollInterval > 0) {
                Log.i("Queuing " + getName() + " every " + pollInterval + " minutes");

                PeriodicWorkRequest workRequest =
                        new PeriodicWorkRequest.Builder(WorkerPoll.class, pollInterval, TimeUnit.MINUTES)
                                .build();
                WorkManager.getInstance(context)
                        .enqueueUniquePeriodicWork(getName(), ExistingPeriodicWorkPolicy.REPLACE, workRequest);

                Log.i("Queued " + getName());
            } else {
                Log.i("Cancelling " + getName());
                WorkManager.getInstance(context).cancelUniqueWork(getName());
                Log.i("Cancelled " + getName());
            }
        } catch (IllegalStateException ex) {
            // https://issuetracker.google.com/issues/138465476
            Log.w(ex);
        }
    }

    static void sync(Context context, Long aid) {
        DB db = DB.getInstance(context);
        try {
            db.beginTransaction();

            List<EntityAccount> accounts = db.account().getSynchronizingAccounts();
            for (EntityAccount account : accounts)
                if (aid == null || account.id.equals(aid)) {
                    List<EntityFolder> folders = db.folder().getSynchronizingFolders(account.id);
                    if (folders.size() > 0)
                        Collections.sort(folders, folders.get(0).getComparator(context));
                    for (EntityFolder folder : folders)
                        EntityOperation.sync(context, folder.id, false);
                }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        ServiceSynchronize.eval(context, "refresh/poll");
    }

    private static String getName() {
        return WorkerPoll.class.getSimpleName();
    }
}
