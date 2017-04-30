package com.cine.views.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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


public class MyWallActivity extends AppCompatActivity implements ICallBack<String> {

    @BindView(R.id.myWallFeedView)
    public RecyclerView myWallFeedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wall);
        ButterKnife.bind(this);
        apiCallWallPost();
    }

    /*API CALL - WALL POST*/
    private void apiCallWallPost() {
        Loader.showProgressBar(this);
        Params params=new Params();

        params.addParam("cg_api_req_name","getposts");
        params.addParam("cg_username","prabu944");
        WebServiceWrapper.getInstance().callService(this, WebService.WALLPOST, params, this);
    }

    private void dismissLoader() {
        Loader.dismissProgressBar();
    }

    @Override
    public void onSuccess(String response) {

        LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
        setMyWallFeedAdapter();
        dismissLoader();

    }

    @Override
    public void onFailure(String response) {
        updateErrorUI(response);
        dismissLoader();
    }

    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(this, errorMsg);
    }

    private void setMyWallFeedAdapter() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        myWallFeedView.setLayoutManager(mLayoutManager);
        myWallFeedView.setFocusable(false);
        HomeFeedAdapter adapter =new HomeFeedAdapter(LocalStorage.feedModel.getCommonwall_posts(),this,false);
        myWallFeedView.setAdapter(adapter);
    }
}
