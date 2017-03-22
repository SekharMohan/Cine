package com.cine.views.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;

import com.cine.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpOne extends AppCompatActivity {
    @BindView(R.id.cineProfessionistButton)
    public AppCompatButton cineProfessionistButton;
    @BindView(R.id.fansClubButton)
    public   AppCompatButton fansClubButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_one);
        ButterKnife.bind(this);
        cineProfessionistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEvent();
            }
        });
        fansClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEvent();
            }
        });
    }

    void onClickEvent(){
        startActivity(new Intent(this,SingUpFinal.class));
    }
}
