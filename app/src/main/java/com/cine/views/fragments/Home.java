package com.cine.views.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.cine.service.model.Alerts;
import com.cine.service.model.FeedModel;
import com.cine.service.model.userinfo.User;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.AppConstants;
import com.cine.utils.AppUtils;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
import com.cine.views.activity.MainActivity;
import com.cine.views.widgets.HomeFeedAdapter;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


public class Home extends Fragment implements ICallBack<String>, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.feedView)
    public RecyclerView homeFeedView;

/*
    @BindView(R.id.pickImage)
    public Button pickImage;

    @BindView(R.id.pickVideo)
    public Button pickVideo;
*/
    @BindView(R.id.alertRelLayout)
    RelativeLayout alertsRelativeLayout;
    @BindView(R.id.alertImage)
    ImageView alertsImage;
    @BindView(R.id.alertDescription)
    AppCompatTextView alertsDescription;
    @BindView(R.id.alertsTitle)
    AppCompatTextView alertsTitle;
    @BindView(R.id.pull_to_refresh)
    SwipeRefreshLayout pullToRefresh;

    private static final int SELECT_PICTURE = 100;
    CineApplication app =  CineApplication.getInstance();
    User info;
    int languageId;

    public Home() {
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
        View rootView = inflater.inflate(R.layout.fragment_home,
                container, false);
        ButterKnife.bind(this,rootView);
        init();


        return rootView;
    }


    @SuppressLint("NewApi")
    private void setAlertsValue() {
        if(app.getAlertsList()!=null) {
            //ToastUtil.showErrorUpdate(getContext(), app.getAlertsList().get(0).getAlert_title());
            if(app.getAlertsList().get(0).getAlert_tyoe().equals("information")) {
                alertsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.alertinfo));
            }else if(app.getAlertsList().get(0).getAlert_tyoe().equals("warning")){
                alertsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.alerwarning));
            }else if(app.getAlertsList().get(0).getAlert_tyoe().equals("success")){
                alertsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.alertsuccess));
            }else if(app.getAlertsList().get(0).getAlert_tyoe().equals("danger")){
                alertsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.alertdanger));
            }
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
        dismissLoader();
    }


    private void init() {

        pullToRefresh.setOnRefreshListener(this);
        pullToRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.colorErro),ContextCompat.getColor(getContext(),R.color.color_app),ContextCompat.getColor(getContext(),R.color.material_teal));
    }

    private void apiCall() {
        Loader.showProgressBar(getContext());
        Params params=new Params();

        params.addParam("cg_api_req_name","getposts");
        params.addParam("cg_username",info.getCg_info().getCgusername());
        WebServiceWrapper.getInstance().callService(getContext(), WebService.FEEDS_URL,params,this);
    }
    @Override
    public void onResume() {
        super.onResume();
       // ToastUtil.showErrorUpdate(getContext(), "onResume of HomeFragment");
        if(AppConstants.isFromLanguage) {
            if (app.getUserInfo() != null) {
                info = app.getUserInfo();
                apiCall();
                alertsApiCall();
            }
        }/*else{
            AppConstants.isFromLanguage = true;
        }*/
    }

    private void setFeedAdapter() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        homeFeedView.setLayoutManager(mLayoutManager);
        HomeFeedAdapter adapter =new HomeFeedAdapter(LocalStorage.feedModel.getCommonwall_posts(),getContext(), true, LocalStorage.feedModel.getUser_datas());
        homeFeedView.setAdapter(adapter);
    }

    @Override
    public void onSuccess(String response) {
        LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
        setFeedAdapter();
        //dismissLoader();
        dimissSwipeLayout();

    }

    @Override
    public void onFailure(String response) {
        updateErrorUI(response);
        dimissSwipeLayout();
        dismissLoader();
    }

    private void dismissLoader() {
        Loader.dismissProgressBar();
    }

    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(getContext(), errorMsg);
    }

    private void alertsApiCall(){
        //Loader.showProgressBar(getContext());
        Params query = new Params();

        final CineApplication app =  CineApplication.getInstance();
        query.addParam("cg_api_req_name", "getpagealerts");
        query.addParam("cg_user_name", info.getCg_info().getCgusername());
        WebServiceWrapper.getInstance().callService(getContext(), WebService.ALERTS_URL, query, new ICallBack<String>() {
            @Override
            public void onSuccess(String response) {
                dismissLoader();
                try {
                    app.setAlertsList((List<Alerts>) new Gson().fromJson(response,new TypeToken<ArrayList<Alerts>>(){}.getType()));
                setAlertsValue();
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

    @Override
    public void onRefresh() {
        apiCall();
        alertsApiCall();
    }

    private void dimissSwipeLayout(){
        if(pullToRefresh.isRefreshing()){
            pullToRefresh.setRefreshing(false);
        }
    }
}
