package com.cine.utils;

import android.content.pm.PackageInfo;

import com.cine.CineApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vijayarvind.j on 17-03-2017.
 */

public class AppUtils {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    private static final int WEEK = 7 * DAY;

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

    public static String getDateFromMilliSeconds(String milliSeconds){

        long ms = Long.parseLong(milliSeconds);
        StringBuffer text = new StringBuffer("");
        if (ms > WEEK) {
            text.append(ms / WEEK).append(" weeks ago");
            return text.toString();
        }
        if (ms > DAY) {
            text.append(ms / DAY).append(" days ago");
            return text.toString();
        }
        if (ms > HOUR) {
            text.append(ms / HOUR).append(" hours ago");
            return text.toString();
        }
        if (ms > MINUTE) {
            text.append(ms / MINUTE).append(" minutes ago");
            return text.toString();
        }
        if (ms > SECOND) {
            text.append(ms / SECOND).append(" seconds ago");
            return text.toString();
        }
        return text.toString();
    }

    public static String getLanguage(String languageID){
        String language = "";
        switch (Integer.parseInt(languageID)){

                case 1:
                    language = "Hindi";
                    break;
                case 2:
                    language = "Kannada";
                    break;
                case 3:
                    language = "Malayalam";
                    break;
                case 4:
                    language = "Tamil";
                    break;

                case 5:
                    language = "Telugu";
                    break;

            }
            return language;


    }
}
