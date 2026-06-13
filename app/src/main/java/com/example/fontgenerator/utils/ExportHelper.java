package com.example.fontgenerator.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import java.io.OutputStream;

public class ExportHelper {

    private static final String TAG = "ExportHelper";

    public static void exportBitmap(Context context, Bitmap bitmap) {
        Log.d(TAG, "exportBitmap called");
        Log.d(TAG, "Bitmap: " + bitmap);

        if (bitmap == null) {
            Toast.makeText(context, "Error: Bitmap is null!", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Bitmap is null!");
            return;
        }

        Log.d(TAG, "Bitmap size: " + bitmap.getWidth() + "x" + bitmap.getHeight());

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME,
                    "handwriting_" + System.currentTimeMillis() + ".png");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES);
            }

            Uri uri = context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Log.d(TAG, "URI: " + uri);

            if (uri == null) {
                Toast.makeText(context, "Failed to create file!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "URI is null!");
                return;
            }

            OutputStream out = context.getContentResolver().openOutputStream(uri);
            if (out == null) {
                Toast.makeText(context, "Failed to open stream!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "OutputStream is null!");
                return;
            }

            boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();

            Log.d(TAG, "Compress success: " + success);

            if (success) {
                Toast.makeText(context, "✅ Exported to Pictures folder!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "❌ Compress failed!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Export error: " + e.getMessage(), e);
            Toast.makeText(context, "Export error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}