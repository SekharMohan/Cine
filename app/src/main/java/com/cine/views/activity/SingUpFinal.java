package com.cine.views.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.cine.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingUpFinal extends AppCompatActivity {

    @BindView(R.id.createMyAccountButton)
    public AppCompatButton createMyAccountButton;
    public static Intent getStartIntent(Context context) {
        return new Intent(context, SingUpFinal.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_two);
        ButterKnife.bind(this);
        createMyAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEvent();
            }
        });
    }


    void onClickEvent(){
        startActivity(new Intent(this,LoginActivity.class));
    }
}
