package com.cine;

import android.app.Application;
import android.os.Handler;

import com.cine.service.model.userinfo.User;
import com.cine.service.model.userinfo.UserPersonal;

import java.util.List;

/**
 * Created by vijayarvind.j on 17-03-2017.
 */

public class CineApplication extends Application {

    private static CineApplication mCineApplication;
    private static User info;
    private static List<UserPersonal> userPersonal;

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
public void setUserPersonal(List<UserPersonal> personal){
    userPersonal = personal;
}

public List<UserPersonal> getUserPersonal(){
    return userPersonal;
}

}
