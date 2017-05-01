package com.cine;

import android.app.Application;
import android.os.Handler;

import com.cine.service.model.userinfo.User;

/**
 * Created by vijayarvind.j on 17-03-2017.
 */

public class CineApplication extends Application {

    private static CineApplication mCineApplication;
    private static User info;

    private static Handler mApplicationHandler;

    public static CineApplication getInstance() {
        if(mCineApplication == null){
            mCineApplication = new CineApplication();
        }
        return mCineApplication;
    }

    public static Handler getApplicationHandler() {
        return mApplicationHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InItInitializer();
    }

    private void InItInitializer() {

        mApplicationHandler = new Handler();

    }

    public void setUserInfo(User info){
        this.info = info;
    }

    public User getUserInfo(){
        return  info;
    }

}
