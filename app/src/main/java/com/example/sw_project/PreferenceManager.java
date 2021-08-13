//package com.example.sw_project;
//
//import android.content.SharedPreferences;
//
//import com.google.api.Context;
//
//public class PreferenceManager {
//
//    public static final String PREFERENCES_NAME = "rebuild_preference";
//
//    private static final String DEFAULT_VALUE_STRING = "";
//
//
//    private static SharedPreferences getPreferences(Context context) {
//
//        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
//
//    }
//
//    public static void setString(Context context, String key, String value) {
//
//        SharedPreferences prefs = getPreferences(context);
//
//        SharedPreferences.Editor editor = prefs.edit();
//
//        editor.putString(key, value);
//
//        editor.commit();
//
//    }
//
//
//}
