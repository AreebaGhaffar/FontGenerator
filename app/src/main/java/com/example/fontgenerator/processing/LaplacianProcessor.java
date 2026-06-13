package com.example.fontgenerator.processing;

import android.graphics.Bitmap;
import android.graphics.Color;

public class LaplacianProcessor {

    public static Bitmap sharpen(Bitmap original) {
        int width = original.getWidth();
        int height = original.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int[][] mask = {
                {-1, -1, -1},
                {-1,  8, -1},
                {-1, -1, -1}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                float lapR = 0, lapG = 0, lapB = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = original.getPixel(x + kx, y + ky);
                        int maskVal = mask[ky + 1][kx + 1];
                        lapR += Color.red(pixel) * maskVal;
                        lapG += Color.green(pixel) * maskVal;
                        lapB += Color.blue(pixel) * maskVal;
                    }
                }

                int origPixel = original.getPixel(x, y);
                int newR = clamp(Color.red(origPixel) - (int) lapR);
                int newG = clamp(Color.green(origPixel) - (int) lapG);
                int newB = clamp(Color.blue(origPixel) - (int) lapB);

                result.setPixel(x, y, Color.rgb(newR, newG, newB));
            }
        }
        return result;
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}