package com.extremex.openlauncher.extxAIM;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableCompat;

public class WallpaperBrightnessChecker {

    private static final double LUMINANCE_THRESHOLD = 128.0;

    /**
     * Determines if the current wallpaper is dark.
     *
     * @param context The application context.
     * @return True if the wallpaper is dark, false if it is light.
     */
    public static boolean isWallpaperDark(Context context) {
        try {
            // Get the current wallpaper
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            Drawable wallpaperDrawable = wallpaperManager.getDrawable();

            // Convert Drawable to Bitmap
            Bitmap wallpaperBitmap = drawableToBitmap(wallpaperDrawable);

            // Check if the Bitmap is dark
            return isBitmapDark(wallpaperBitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Default to light if there's an error
        }
    }

    /**
     * Converts a Drawable to a Bitmap.
     *
     * @param drawable The Drawable to convert.
     * @return The resulting Bitmap.
     */
    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Determines if the given Bitmap is dark based on its luminance.
     *
     * @param bitmap The Bitmap to check.
     * @return True if the Bitmap is dark, false if it is light.
     */
    private static boolean isBitmapDark(Bitmap bitmap) {
        long totalLuminance = 0;
        int pixelCount = 0;

        // Loop through each pixel
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                // Get the RGB value of the pixel
                int rgb = bitmap.getPixel(x, y);

                // Extract the color components
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Calculate the luminance using the formula
                double luminance = 0.299 * red + 0.587 * green + 0.114 * blue;

                // Accumulate the luminance
                totalLuminance += luminance;
                pixelCount++;
            }
        }

        // Calculate the average luminance
        double averageLuminance = totalLuminance / (double) pixelCount;

        // Return true if the average luminance is less than the threshold (dark image)
        return averageLuminance < LUMINANCE_THRESHOLD;
    }
}
