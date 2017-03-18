package com.cine.views.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cine.R;
import com.rntbci.networklib.NetworkUtil;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        test();
    }

    private void test() {
      boolean test=  NetworkUtil.isConnected(this);

        System.out.println();
    }
}
