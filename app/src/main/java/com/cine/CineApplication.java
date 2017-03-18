package com.cine;

import android.app.Application;
import android.os.Handler;

/**
 * Created by vijayarvind.j on 17-03-2017.
 */

public class CineApplication extends Application {

    private static CineApplication mCineApplication;

    private static Handler mApplicationHandler;

    public static Application getInstance() {
        return mCineApplication;
    }

    public static Handler getApplicationHandler() {
        return mApplicationHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCineApplication = this;
        InItInitializer();
    }

    private void InItInitializer() {

        mApplicationHandler = new Handler();

    }

}
