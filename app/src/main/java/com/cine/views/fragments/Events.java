package com.cine.views.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.EventsModel;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.AppConstants;
import com.cine.utils.AppUtils;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
import com.cine.views.widgets.EventsAdapter;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Events extends Fragment implements ICallBack<String> {

    @BindView(R.id.evRecyclerView)
    RecyclerView evRecyclerView;
    @BindView(R.id.evalertRelLayout)
    RelativeLayout alertsRelativeLayout;
    @BindView(R.id.evalertImage)
    ImageView alertsImage;
    @BindView(R.id.evalertDescription)
    AppCompatTextView alertsDescription;
    @BindView(R.id.evalertsTitle)
    AppCompatTextView alertsTitle;
    private CineApplication app = CineApplication.getInstance();

    public Events() {
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
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this,view);
        //apiCall();
        setAlertsValue();
        return  view;
    }

    @SuppressLint("NewApi")
    private void setAlertsValue() {
        if(app.getAlertsList()!=null) {
            //ToastUtil.showErrorUpdate(getContext(), app.getAlertsList().get(0).getAlert_title());
            alertsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.alertinfo));
            String textColor = AppUtils.getAlertTextColor(app.getAlertsList().get(0).getAlert_tyoe());
            alertsTitle.setText(app.getAlertsList().get(0).getAlert_title());
            alertsTitle.setTextColor(Color.parseColor(textColor));
            alertsDescription.setText(app.getAlertsList().get(0).getAlert_description());
            alertsDescription.setTextColor(Color.parseColor(textColor));
            if (app.getAlertsList().get(0).getAlert_picture() != null) {
                alertsImage.setVisibility(View.VISIBLE);
            } else {
                alertsImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppConstants.isFromLanguage) {
            apiCall();
        }
    }

    private void apiCall() {
        Loader.showProgressBar(getContext());
        Params params=new Params();

        params.addParam("cg_api_req_name","getevents");
        params.addParam("cg_user_name","prabu944");
        WebServiceWrapper.getInstance().callService(getContext(), WebService.EVENTSURL,params,this);
    }

    @Override
    public void onSuccess(String response) {
        dismissLoader();
        ArrayList<EventsModel> eventsList = new Gson().fromJson(response,new TypeToken<ArrayList<EventsModel>>(){}.getType());
        if(eventsList!=null) {
            setFeedAdapter(eventsList);

        }else{
            ToastUtil.showErrorUpdate(getContext(), "No events found");
        }
    }

    private void setFeedAdapter(ArrayList<EventsModel> eventsList) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        evRecyclerView.setLayoutManager(mLayoutManager);
        EventsAdapter adsAdapter =new EventsAdapter(eventsList,getContext());
        evRecyclerView.setAdapter(adsAdapter);
    }


    @Override
    public void onFailure(String response) {
        dismissLoader();
        updateErrorUI(response);
    }

    private void dismissLoader() {
        Loader.dismissProgressBar();
    }

    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(getContext(), errorMsg);
    }
}
