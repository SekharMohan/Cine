package com.cine.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.cine.CineApplication;

/**
 * Created by DELL on 30-04-2017.
 */

public class PrefUtils {

    private static final String APP_PREF_FILE_NAME = "cine_gate_application";

    private final static String KEY_LANGUAGE_ID = "cine_gate_application_lang_id";

    private static SharedPreferences mApplicationSharedPreference;

    static {
        mApplicationSharedPreference = CineApplication.getInstance().getSharedPreferences(APP_PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static int getLanguageValue() {
        return mApplicationSharedPreference.getInt(KEY_LANGUAGE_ID, -1);
    }

    public static void setLanguageValue(int languageValue) {
        SharedPreferences.Editor editor = mApplicationSharedPreference.edit();
        editor.putInt(KEY_LANGUAGE_ID, languageValue);
        editor.apply();
    }
}
