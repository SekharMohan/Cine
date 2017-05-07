package com.cine.views.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.cine.CineApplication;
import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.model.SearchModel;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.AppConstants;
import com.cine.utils.ItemClickListener;
import com.cine.utils.ToastUtil;
import com.cine.views.widgets.SearchUsersAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 08-05-2017.
 */

public class SearchUsersActivity extends Activity implements TextWatcher, ICallBack<String>,ItemClickListener {

    @BindView(R.id.searchEditText)
    EditText searchEt;
    @BindView(R.id.searchRecyclerView)
    RecyclerView searchRv;

    private SearchUsersAdapter searchAdapter;
    private ArrayList<SearchModel> searchList;
    CineApplication app = CineApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.logo_small);
        setContentView(R.layout.search_pop_up);
        AppConstants.isFromLanguage = true;
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchList = new ArrayList<>();
        searchEt.addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().length() > 0) {
            sendRequest(charSequence.toString());
        }
    }

    @Override
    public void onClick(View view, int position) {
        String selectedUserName = searchList.get(position).getSearch_username();
        Intent wallIntent = new Intent(this, MyWallActivity.class);
        wallIntent.putExtra("username", selectedUserName);
        startActivity(wallIntent);
        finish();
    }


    private void sendRequest(String searchText) {
        Params params=new Params();

        params.addParam("cg_api_req_name","searchusers");
        params.addParam("cg_user_name",app.getUserInfo().getCg_info().getCgusername());
        params.addParam("cg_searchtext",searchText);
        WebServiceWrapper.getInstance().callService(this, WebService.SEARCH_URL,params,this);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onSuccess(String response) {
        searchList = new Gson().fromJson(response,new TypeToken<ArrayList<SearchModel>>(){}.getType());
        if(searchList!=null) {
            searchRv.setVisibility(View.VISIBLE);
            setFeedAdapter(searchList);

        }else{
            ToastUtil.showErrorUpdate(this, "No users found");
            searchRv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String response) {

    }

    private void setFeedAdapter(ArrayList<SearchModel> searchResultList) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        searchRv.setLayoutManager(mLayoutManager);
        searchAdapter = new SearchUsersAdapter(searchResultList,this);
        searchRv.setAdapter(searchAdapter);
        searchAdapter.setClickListener(this);
    }

}
