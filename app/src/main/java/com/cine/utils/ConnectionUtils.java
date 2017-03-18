package com.cine.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cine.CineApplication;



public class ConnectionUtils {
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) CineApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
