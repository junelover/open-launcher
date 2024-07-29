package com.extremex.openlauncher.activity;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.extremex.openlauncher.R;
import com.extremex.openlauncher.fragment.HomeFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();

        // Request read external storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Permission already granted
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
}
