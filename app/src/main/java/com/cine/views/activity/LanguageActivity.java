package com.cine.views.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
import com.cine.service.model.userinfo.User;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.LocalStorage;
import com.cine.utils.PrefUtils;
import com.cine.utils.ToastUtil;
import com.cine.views.widgets.HomeFeedAdapter;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 01-05-2017.
 */

public class LanguageActivity extends AppCompatActivity implements ICallBack<String> {

    @BindArray(R.array.language)
    String[] arrLanguage;
    @BindView(R.id.languageSelectionSpinner)
    AppCompatSpinner spiLanguage;
    @BindView(R.id.languageFeedView)
    RecyclerView languageFeedView;
    Map<Integer,String> languageAndId = new HashMap<>();

    CineApplication app =  CineApplication.getInstance();
    User info;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_language);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if(app.getUserInfo()!=null){
            info = app.getUserInfo();
            apiCall();
        }
        int length = arrLanguage.length;
        for (int i = 0; i < length; i++) {
            languageAndId.put((i+1), arrLanguage[i]);
        }

        spinnerSetup(spiLanguage, arrLanguage, getString(R.string.language_hint));
        spiLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(++check > 1) {
                    int selectedLanguageId = position + 1;
                    updateLanguage(String.valueOf(selectedLanguageId));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateLanguage(String languageId){

        Params params = new Params();

        params.addParam("cg_api_req_name", "change_current_state");
        //params.addParam("cg_user_name", app.getUserInfo().getCg_info().getCgusername());
        params.addParam("cg_ur_name", info.getCg_info().getCgusername());
        params.addParam("cg_state_id", languageId);

        WebServiceWrapper.getInstance().callService(this, WebService.FEEDS_URL, params, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                dismissLoader();
                try {

                    JSONObject json = new JSONObject(response);
                    if(json.getString("cg_state_change_msg").equals("Language Changed Successfully")) {
                        updateErrorUI("Language Changed Successfully");
                        apiCall();
                    }else {
                        updateErrorUI("Language update failed");
                    }

                }catch (Exception e){
                    updateErrorUI("Language update failed");

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String response) {
                dismissLoader();
                updateErrorUI(response);
            }
        });
    }

    private void apiCall() {
        Params params=new Params();

        params.addParam("cg_api_req_name","getposts");
        params.addParam("cg_username", info.getCg_info().getCgusername());
        WebServiceWrapper.getInstance().callService(this, WebService.FEEDS_URL,params,this);
    }

    public void spinnerSetup(AppCompatSpinner spinner,String[] values,String hint){
        int lanugaugeId = Integer.parseInt(info.getCg_info().getUser_languageid());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(values);
        adapter.add(hint);
        spinner.setAdapter(adapter);
        spinner.setSelection(lanugaugeId-1);

    }


    @Override
    public void onSuccess(String response) {
        LocalStorage.feedModel = new Gson().fromJson(response, FeedModel.class);
        setFeedAdapter();
        dismissLoader();
    }

    @Override
    public void onFailure(String response) {
        updateErrorUI(response);
        dismissLoader();;

    }

    private void dismissLoader() {
        Loader.dismissProgressBar();
    }

    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(this, errorMsg);
    }

    private void setFeedAdapter() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        languageFeedView.setLayoutManager(mLayoutManager);
        HomeFeedAdapter adapter =new HomeFeedAdapter(LocalStorage.feedModel.getCommonwall_posts(),this, false);
        languageFeedView.setAdapter(adapter);
    }
}
