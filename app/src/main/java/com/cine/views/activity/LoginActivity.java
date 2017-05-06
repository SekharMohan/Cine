package com.cine.views.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.Alerts;
import com.cine.service.model.userinfo.User;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.ToastUtil;
import com.cine.utils.ValidationUtil;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ICallBack<String> {
    @BindView(R.id.signUPButton)
    public Button btnSignUp;
    @BindView(R.id.loginButton)
    public Button btnLogin;
    @BindView(R.id.loginPassword)
    public EditText edtPassword;
    @BindView(R.id.loginUserName)
    public EditText edtUserName;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);


    }

    void goToDashBoard() {
        startActivity(MainActivity.getStartIntent(this));
    }

    void goToSignUP() {
        startActivity(SignUpOne.getStartIntent(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUPButton:
                goToSignUP();
                break;

            case R.id.loginButton:
                loginApi();
                break;


        }

    }

    private void alertsApiCall(){
        //Loader.showProgressBar(getContext());
        Params query = new Params();

        final CineApplication app =  CineApplication.getInstance();
        query.addParam("cg_api_req_name", "getpagealerts");
        query.addParam("cg_user_name", userName);
        WebServiceWrapper.getInstance().callService(this, WebService.ALERTS_URL, query, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                dismissLoader();
                try {
                    app.setAlertsList((List<Alerts>) new Gson().fromJson(response,new TypeToken<ArrayList<Alerts>>(){}.getType()));

                    // ArrayList<Alerts> eventsList = new Gson().fromJson(response,new TypeToken<ArrayList<Alerts>>(){}.getType());
                    //app.setAlertsList(eventsList);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailure(String response) {
                updateErrorUI(response);
                dismissLoader();
            }
        });
    }



    public void loginApi() {

        userName = edtUserName.getText().toString();
        String psw = edtPassword.getText().toString();

        if (!ValidationUtil.checkEmptyFields(userName, psw)) {

            Loader.showProgressBar(this);
            Params requestQuery = new Params();
            requestQuery.addParam("cg_uname",userName);
            requestQuery.addParam("cg_upwd", psw);
            WebServiceWrapper.getInstance().callServicePost(this, WebService.SIGNIN_URL, requestQuery, this);
        } else {
            updateErrorUI(getString(R.string.login_field_empty_warning));
        }
    }

    @Override
    public void onSuccess(String response) {
        dismissLoader();
        try {

            JSONObject json = new JSONObject(response);
            if(json.getString("cg_log_status").equals("1")) {
               CineApplication app =  CineApplication.getInstance();
                app.setUserInfo(new Gson().fromJson(response, User.class));
                alertsApiCall();
                goToDashBoard();
                finish();
            }else {
                updateErrorUI("Login Failed. Pls verify credentials");
            }

        }catch (Exception e){
            updateErrorUI("Login Failed. Pls verify credentials");

            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(String response) {
        updateErrorUI(response);
        dismissLoader();
    }

    private void dismissLoader() {
        Loader.dismissProgressBar();
    }

    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(this, errorMsg);
    }
}
