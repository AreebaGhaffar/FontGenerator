package com.example.fontgenerator.utils;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class StorageHelper {

    private static final String PREFS_NAME = "FontGeneratorPrefs";

    public static void saveLetter(Context context, String letter, List<List<float[]>> strokes) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("letter", letter);
            JSONArray strokesArray = new JSONArray();
            for (List<float[]> stroke : strokes) {
                JSONArray strokeArray = new JSONArray();
                for (float[] point : stroke) {
                    JSONArray pointArray = new JSONArray();
                    pointArray.put(point[0]);
                    pointArray.put(point[1]);
                    strokeArray.put(pointArray);
                }
                strokesArray.put(strokeArray);
            }
            obj.put("strokes", strokesArray);
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString("letter_" + letter, obj.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<List<float[]>> loadLetter(Context context, String letter) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString("letter_" + letter, null);
        if (json == null) return null;
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray strokesArray = obj.getJSONArray("strokes");
            List<List<float[]>> strokes = new ArrayList<>();
            for (int i = 0; i < strokesArray.length(); i++) {
                JSONArray strokeArray = strokesArray.getJSONArray(i);
                List<float[]> stroke = new ArrayList<>();
                for (int j = 0; j < strokeArray.length(); j++) {
                    JSONArray point = strokeArray.getJSONArray(j);
                    stroke.add(new float[]{(float) point.getDouble(0), (float) point.getDouble(1)});
                }
                strokes.add(stroke);
            }
            return strokes;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isLetterSaved(Context context, String letter) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.contains("letter_" + letter);
    }
}