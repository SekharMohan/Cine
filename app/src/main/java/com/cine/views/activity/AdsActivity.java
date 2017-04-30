package com.cine.views.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.AdsModel;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.ToastUtil;
import com.cine.views.widgets.AdsAdapter;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdsActivity extends AppCompatActivity implements ICallBack<String> {

    @BindView(R.id.adRecyclerView)
    public RecyclerView adRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        ButterKnife.bind(this);
        apiCall();
        
    }

    private void apiCall() {
        Loader.showProgressBar(this);
        Params params=new Params();

        params.addParam("cg_api_req_name","getadminad");
        params.addParam("cg_user_name","prabu944");
        WebServiceWrapper.getInstance().callService(this, WebService.ADPOST,params,this);
    }

    @Override
    public void onSuccess(String response) {
        ArrayList<AdsModel> adsList = new Gson().fromJson(response,new TypeToken<ArrayList<AdsModel>>(){}.getType());
        //ArrayList<AdsModel> adsList = new ArrayList<>();
       // adsList.add(adsModel);
        setFeedAdapter(adsList);

        dismissLoader();
    }

    private void setFeedAdapter(ArrayList<AdsModel> adsList) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        adRecyclerView.setLayoutManager(mLayoutManager);
        AdsAdapter adsAdapter =new AdsAdapter(adsList,this);
        adRecyclerView.setAdapter(adsAdapter);
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
