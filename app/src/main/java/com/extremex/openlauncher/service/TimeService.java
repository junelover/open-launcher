package com.extremex.openlauncher.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class TimeService extends Service {

    public static final String TIME_UPDATED = "TIME_UPDATED";
    public static final String DEVICE_TIME = "DEVICE_TIME";
    private final String FORMAT_12 = "hh:mm";
    private final String FORMAT_24 = "HH:mm";

    private Timer timer;
    private LocalBroadcastManager localBroadcastManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimeTask(), 0, 1000); // Update every minute

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }

    private class TimeTask extends TimerTask {
        @Override
        public void run() {
            String time = getCurrentTime( FORMAT_12 ) + getAmPm().toLowerCase();

            Intent intent = new Intent(DEVICE_TIME);
            intent.putExtra(TIME_UPDATED, time);
            localBroadcastManager.sendBroadcast(intent);
        }
    }

    // Method to get the current device time as a formatted string
    public String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    // Method to get the current device time in a specific format
    public String getCurrentTime(String format) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return now.format(formatter);
    }

    // Method to determine if the current time is AM or PM
    public String getAmPm() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        return (hour < 12) ? "AM" : "PM";
    }
}
