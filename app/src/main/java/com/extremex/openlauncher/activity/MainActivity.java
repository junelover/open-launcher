package com.extremex.openlauncher.activity;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.extremex.openlauncher.data.SharedData;
import com.extremex.openlauncher.service.TimeService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.extremex.openlauncher.R;
import com.extremex.openlauncher.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;

    private HomeFragment homeFragment;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter timeIntentFilter;

    private BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String time = intent.getStringExtra(TimeService.TIME_UPDATED);
            SharedData.setTime(time);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        homeFragment = new HomeFragment();

        // Request read external storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Permission already granted
            timeIntentFilter = new IntentFilter(TimeService.DEVICE_TIME);

            localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.registerReceiver(timeReceiver, timeIntentFilter);

            Intent serviceIntent = new Intent(this, TimeService.class);
            startService(serviceIntent);

            initializeApp();
        }
    }

    private void initializeApp() {
        ImageView wallArt = findViewById(R.id.WallArt);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            try {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                Drawable wallpaperDrawable = wallpaperManager.getDrawable();
                wallArt.setImageDrawable(wallpaperDrawable);
            } catch (SecurityException e) {
                // Fallback if permission not granted
                wallArt.setImageResource(R.drawable.test_wall);
            }
        } else {
            // Older Android versions, just use the wallpaper drawable
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            Drawable wallpaperDrawable = wallpaperManager.getDrawable();
            wallArt.setImageDrawable(wallpaperDrawable);
        }

        loadFragment(homeFragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFrame, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.MainFrame) == homeFragment) {
            // Do nothing or handle as needed
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize the app
                initializeApp();
            } else {
                // Permission denied, handle as appropriate
                // You might want to show a message to the user or disable certain functionality
                ImageView wallArt = findViewById(R.id.WallArt);
                wallArt.setImageResource(R.drawable.test_wall);
                loadFragment(homeFragment);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Permission already granted
        timeIntentFilter = new IntentFilter(TimeService.DEVICE_TIME);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(timeReceiver, timeIntentFilter);

        Intent serviceIntent = new Intent(this, TimeService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        // Permission already granted
        timeIntentFilter = new IntentFilter(TimeService.DEVICE_TIME);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(timeReceiver, timeIntentFilter);

        Intent serviceIntent = new Intent(this, TimeService.class);
        startService(serviceIntent);
        super.onResume();
    }

    @Override
    protected void onPause() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(timeReceiver);
        Intent serviceIntent = new Intent(this, TimeService.class);
        stopService(serviceIntent);
        super.onPause();
    }

    @Override
    protected void onStop() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(timeReceiver);
        Intent serviceIntent = new Intent(this, TimeService.class);
        stopService(serviceIntent);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        // Permission already granted
        timeIntentFilter = new IntentFilter(TimeService.DEVICE_TIME);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(timeReceiver, timeIntentFilter);

        Intent serviceIntent = new Intent(this, TimeService.class);
        startService(serviceIntent);
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(timeReceiver);
        Intent serviceIntent = new Intent(this, TimeService.class);
        stopService(serviceIntent);
        super.onDestroy();
    }
}
