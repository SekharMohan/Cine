package com.cine.utils;

import android.util.Log;


import java.util.List;
import java.util.Map;

public class LogUtils {
    public static void logRequestDetails(String loginUrl, String jsonResponse) {
        try {
            Log.i(AppConstants.BASE_TAG, "Generated URL --> " + loginUrl);
            Log.i(AppConstants.BASE_TAG, "server response --->" + jsonResponse + "<--");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logPostDetails(String jobStatusUpdateURl, Map<String, String> multipartEntity) {
        try {
            System.out.print(jobStatusUpdateURl);

            for (Map.Entry<String, String> stringStringEntry : multipartEntity.entrySet()) {
                System.out.print(stringStringEntry.getKey() + "=" + stringStringEntry.getValue() + "&");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
