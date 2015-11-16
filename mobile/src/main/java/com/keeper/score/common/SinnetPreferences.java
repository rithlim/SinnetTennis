package com.keeper.score.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rith on 11/15/2015.
 */
public class SinnetPreferences {

    private static SharedPreferences mSharedPreferences;
    private static final String SINNET_PREFERENCES = "SinnetPreferences";

    private static void init(Context context) {
        if(mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SINNET_PREFERENCES, Context.MODE_PRIVATE);
        }
    }

    public static String getPreferences(Context context, String key) {
        if(mSharedPreferences == null) {
            init(context);
        }
        return mSharedPreferences.getString(key, null);
    }

    public static boolean getBooleanPreferences(Context context, String key) {
        if(mSharedPreferences == null) {
            init(context);
        }
        return mSharedPreferences.getBoolean(key, false);
    }

    public static void putPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SINNET_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putPreferences(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SINNET_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
