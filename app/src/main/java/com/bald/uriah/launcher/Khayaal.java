/*
 * Copyright 2024 shyam-king
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bald.uriah.launcher;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bald.uriah.launcher.activities.UpdatesActivity;
import com.bald.uriah.launcher.databases.alarms.AlarmScheduler;
import com.bald.uriah.launcher.databases.reminders.ReminderScheduler;
import com.bald.uriah.launcher.services.NotificationListenerService;
import com.bald.uriah.launcher.utils.BaldUncaughtExceptionHandler;
import com.bald.uriah.launcher.utils.S;

import net.danlew.android.joda.JodaTimeAndroid;

public class Khayaal extends Application {
    private static final String TAG = Khayaal.class.getSimpleName();

    @Override
    public void onCreate() {
        S.logImportant("Khayaal was started!");
        super.onCreate();
        JodaTimeAndroid.init(this);
        AlarmScheduler.reStartAlarms(this);
        ReminderScheduler.reStartReminders(this);
        if (BuildConfig.FLAVOR.equals("baldUpdates")) {
            UpdatesActivity.removeUpdatesInfo(this);
        }
        try {
            startService(new Intent(this, NotificationListenerService.class));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        S.sendVersionInfo(this);
    }

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        Thread.setDefaultUncaughtExceptionHandler(
                new BaldUncaughtExceptionHandler(this, Thread.getDefaultUncaughtExceptionHandler())
        );
    }
}