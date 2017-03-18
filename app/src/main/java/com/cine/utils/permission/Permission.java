package com.cine.utils.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by sekhar on 28/10/16.
 * this class will check runtime permission for our application .
 */

public class Permission {
    private  Activity mActivity;

    public Permission(Activity activity){
        mActivity=activity;
    }

    public boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(mActivity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }
//check whether required permission is had or not
    public boolean hasPermission(String[] permissionsList) {
        for (String permission : permissionsList) {
            if (ActivityCompat.checkSelfPermission(mActivity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
//this will ask user to allow particular permission by showing prompt, if you are running marshmallow OS.
    public void requestPermission(String permission, int requestCode) {
        if (ActivityCompat.checkSelfPermission(mActivity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[] { permission }, requestCode);
        }
    }
}
