package com.cine.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import static android.app.Activity.RESULT_OK;


public class Home extends Fragment implements ICallBack<String>, View.OnClickListener ,SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.feedView)
    public RecyclerView homeFeedView;

    @BindView(R.id.pickImage)
    public Button pickImage;

    @BindView(R.id.pickVideo)
    public Button pickVideo;

    @BindView(R.id.pull_to_refresh)
    SwipeRefreshLayout pullToRefresh;

    private static final int SELECT_PICTURE = 100;

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
        apiCall();

        return rootView;
    }

    private void init() {
        pullToRefresh.setOnRefreshListener(this);
        pullToRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.colorErro),ContextCompat.getColor(getContext(),R.color.color_app),ContextCompat.getColor(getContext(),R.color.material_teal));
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
        HomeFeedAdapter adapter =new HomeFeedAdapter(LocalStorage.feedModel.getCommonwall_posts(),getContext(),getFragmentManager());
        homeFeedView.setAdapter(adapter);
    }

    @Override
    public void onSuccess(String response) {
        LocalStorage.feedModel = new Gson().fromJson(response,FeedModel.class);
        setFeedAdapter();
        dimissSwipeLayout();
        dismissLoader();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pickImage:
                openImageChooser();
                break;

            case R.id.pickVideo:

                break;
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();


            }
        }
    }

    @Override
    public void onRefresh() {
        apiCall();
    }

    private void dimissSwipeLayout(){
        if(pullToRefresh.isRefreshing()){
            pullToRefresh.setRefreshing(false);
        }
    }
}
