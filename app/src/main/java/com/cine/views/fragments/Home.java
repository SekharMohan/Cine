package com.cine.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.FeedModel;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.LocalStorage;
import com.cine.utils.ToastUtil;
import com.cine.views.widgets.HomeFeedAdapter;
import com.cine.views.widgets.Loader;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Home extends Fragment implements ICallBack<String> {

    @BindView(R.id.feedView)
    public RecyclerView homeFeedView;

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
        apiCall();

        return rootView;
    }
    private void apiCall() {
        Loader.showProgressBar(getContext());
        Params params=new Params();

        params.addParam("cg_api_req_name","getposts");
        params.addParam("cg_username","prabu944");
        WebServiceWrapper.getInstance().callService(getContext(), WebService.FEEDS_URL,params,this);
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    private void setFeedAdapter() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        homeFeedView.setLayoutManager(mLayoutManager);
        HomeFeedAdapter adapter =new HomeFeedAdapter(LocalStorage.feedModel.getCommonwall_posts(),getContext());
        homeFeedView.setAdapter(adapter);
    }

    @Override
    public void onSuccess(String response) {
        LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
        setFeedAdapter();
        dismissLoader();
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
