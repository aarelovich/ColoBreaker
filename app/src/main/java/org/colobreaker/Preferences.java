package org.colobreaker;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREFS_NAME       = "settings";
    public static final String  KEY_COLORS       = "color_count";
    public static final String  KEY_CODE_SIZE    = "code_size";
    public static final String  KEY_MAX_SCORE    = "max_score";
    public static final String  KEY_SOUND        = "sound";

    // Save values to SharedPreferences
    public static void Save(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void SaveAsInt(Context context, String key, int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int GetAsInt(Context context, String key, int defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,defValue);
    }

    // Retrieve values from SharedPreferences
    public static String GetPreference(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }


}
