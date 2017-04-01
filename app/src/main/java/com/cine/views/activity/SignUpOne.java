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

public class SignUpOne extends AppCompatActivity {
    @BindView(R.id.cineProfessionistButton)
    public AppCompatButton cineProfessionistButton;
    @BindView(R.id.fansClubButton)
    public   AppCompatButton fansClubButton;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SignUpOne.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_one);
        ButterKnife.bind(this);
        cineProfessionistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEvent(getString(R.string.user_type_professional));
            }
        });
        fansClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEvent(getString(R.string.user_type_fans));
            }
        });
    }

    void onClickEvent(String type){
        Intent intent =  SingUpFinal.getStartIntent(this);
        intent.putExtra("user_type",type);
        startActivity(new Intent(this,SingUpFinal.class));
    }
}
