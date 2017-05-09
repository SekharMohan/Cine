package com.cine.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.AdsModel;
import com.cine.service.model.userinfo.User;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.AppConstants;
import com.cine.utils.ToastUtil;
import com.cine.views.widgets.AdsAdapter;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 30-04-2017.
 */

public class Bonus extends Fragment implements ICallBack<String> {

    @BindView(R.id.adRecyclerView)
    public RecyclerView adRecyclerView;
    CineApplication app = CineApplication.getInstance();
    User info;
    public Bonus() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_ads, container, false);

        ButterKnife.bind(this,view);
        //apiCall();
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // ToastUtil.showErrorUpdate(getContext(), "onResume of HomeFragment");
        if(AppConstants.isFromLanguage) {
            if (app.getUserInfo() != null) {
                info = app.getUserInfo();
                apiCall();

            }
        }
    }

    private void apiCall() {
        //Loader.showProgressBar(getContext());
        Params params=new Params();

        params.addParam("cg_api_req_name","getadminad");
        params.addParam("cg_user_name",info.getCg_info().getCgusername());
        WebServiceWrapper.getInstance().callService(getContext(), WebService.ADPOST,params,this);
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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adRecyclerView.setLayoutManager(mLayoutManager);
        AdsAdapter adsAdapter =new AdsAdapter(adsList,getActivity());
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
        ToastUtil.showErrorUpdate(getContext(), errorMsg);
    }
}
