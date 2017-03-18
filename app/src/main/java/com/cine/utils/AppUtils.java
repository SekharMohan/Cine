package com.cine.utils;

import android.content.pm.PackageInfo;

import com.cine.CineApplication;

/**
 * Created by vijayarvind.j on 17-03-2017.
 */

public class AppUtils {
    public static int getVersionCode() {
        PackageInfo pInfo;
        try {
            pInfo = CineApplication.getInstance().getPackageManager().getPackageInfo(CineApplication.getInstance().getPackageName(), 0);
            return pInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
