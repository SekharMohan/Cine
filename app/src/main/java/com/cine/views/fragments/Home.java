package com.cine.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;

import butterknife.BindView;


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

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onSuccess(String response) {
        System.out.println();

    }

    @Override
    public void onFailure(String response) {
        System.out.println();
    }
}