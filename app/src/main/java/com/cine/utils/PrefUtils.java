package com.cine.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.cine.CineApplication;

/**
 * Created by DELL on 30-04-2017.
 */

public class PrefUtils {

    private static final String APP_PREF_FILE_NAME = "cine_gate_application";

    private final static String KEY_LOGIN_UESRNAME = "cine_gate_application_user_name";
    private final static String KEY_LOGIN_PASSWORD = "cine_gate_application_user_password";

    private static SharedPreferences mApplicationSharedPreference;

    public PrefUtils(Context context) {
        mApplicationSharedPreference = context.getSharedPreferences(APP_PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getUserName() {
        return mApplicationSharedPreference.getString(KEY_LOGIN_UESRNAME, "");
    }

    public void setUserName(String userNameValue) {
        SharedPreferences.Editor editor = mApplicationSharedPreference.edit();
        editor.putString(KEY_LOGIN_UESRNAME, userNameValue);
        editor.apply();
    }

    public String getPassword() {
        return mApplicationSharedPreference.getString(KEY_LOGIN_PASSWORD, "");
    }

    public void setpassword(String passwordValue) {
        SharedPreferences.Editor editor = mApplicationSharedPreference.edit();
        editor.putString(KEY_LOGIN_PASSWORD, passwordValue);
        editor.apply();
    }
}
