package com.cine.service;

import android.content.Context;

import com.cine.R;
import com.cine.service.network.NetworkUtil;
import com.cine.service.network.Params;
import com.cine.service.network.WebServiceRequest;
import com.cine.service.network.callback.ICallBack;

/**
 * Created by Renault Nissan Technology & Business Center India Pvt Ltd
 * Copyright (c) 2017 Renault Nissan Technology & Business Center India Pvt Ltd
 */

public class WebServiceWrapper {

    public static WebServiceWrapper webServiceWrapper;
    private WebServiceWrapper(){

    }
    ICallBack<R> listener;

    public static WebServiceWrapper getInstance(){
        if(webServiceWrapper==null){
            webServiceWrapper=new WebServiceWrapper();
        }
        return webServiceWrapper;
    }

    public void callService(Context mcontext ,String url, Params params, final ICallBack listener) {

        this.listener = listener;
        if (NetworkUtil.isConnected(mcontext)) {
            new WebServiceRequest<String>(WebServiceRequest.RequestMethod.POST, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    listener.onSuccess(response);

                }

                @Override
                public void onFailure(String response) {
                    listener.onFailure(response);

                }
            }, url, params).execute();
        }else {
            listener.onFailure(mcontext.getString(R.string.no_internet));
        }
    }

    public void callServicePost(Context mcontext ,String url, Params params, final ICallBack listener) {

        this.listener = listener;
        if (NetworkUtil.isConnected(mcontext)) {
            new WebServiceRequest<String>(WebServiceRequest.RequestMethod.POSTWITHBODY, new ICallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    listener.onSuccess(response);

                }

                @Override
                public void onFailure(String response) {
                    listener.onFailure(response);

                }
            }, url, params).execute();
        }else {
            listener.onFailure(mcontext.getString(R.string.no_internet));
        }
    }

}
