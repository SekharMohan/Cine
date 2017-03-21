package com.cine.service.network;

import android.os.AsyncTask;
import android.util.Log;

import com.cine.service.network.callback.ICallBack;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;



public class WebServiceRequest<R extends Object> extends
		AsyncTask<Void, Void, Void> {

	ICallBack<R> listener;
	private R response;
	private Params params;

	private RequestMethod requestMethod;
	private String url = null;

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	public static enum RequestMethod {
		GET, POST
	}

	public WebServiceRequest(RequestMethod reqMethod, ICallBack<R> listener,
			String url,Params params) {

		this.requestMethod = reqMethod;
		this.url = url;
		this.params=params;
		this.listener = listener;

	}
	
	public WebServiceRequest(RequestMethod reqMethod, ICallBack<R> listener,
			String url) {

		this.requestMethod = reqMethod;
		this.url = url;
		
		this.listener = listener;

	}

	@Override
	protected Void doInBackground(Void... parms)  {
		// TODO Auto-generated method stub
		try{
		RestService restService = new RestService();

		if (requestMethod == RequestMethod.GET) {
			HttpURLConnection result;
			if(params!=null){

			 result= restService.executeGetRequest(url+params.getParamList());
			}else{
				 result=restService.executeGetRequest(url);
			}
			setResponse(result);
		}

		else {

			HttpURLConnection result= restService.executePostResquest(url,params.getParamList());
			setResponse(result);
		}
		}catch(Exception e){
			try {
				handleException(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		

		
		}
		
		return null;

	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	
	@SuppressWarnings("unchecked")
	private void setResponse(HttpURLConnection result)throws IOException {
		// TODO Auto-generated method stub
		
		BufferedReader updateResponse; 
		
		if(result.getResponseCode()==HttpURLConnection.HTTP_OK){
		Log.d("RestService-Response", result.getResponseCode()+":"+result.getResponseMessage());
		updateResponse=new BufferedReader(new InputStreamReader(result.getInputStream()));
		
		/*response = (R)updateResponse;
		listener.onSuccess(response);*/
		
		response = (R)responseString(updateResponse);
		listener.onSuccess(response);
		
		}
	else{
		Log.e("RestService-Error", result.getResponseCode()+":"+result.getResponseMessage());
		
		updateResponse=new BufferedReader(new InputStreamReader(result.getErrorStream()));
//		response = (R)updateResponse;
		response = (R)responseString(updateResponse);
		listener.onFailure(response);
		
		
	}
		
		
		
		
	}
	@SuppressWarnings("unchecked")
	private void handleException(Exception e)throws IOException{
		String msg=e.getMessage();
		BufferedReader updateResponse; 
		InputStream stream = null;
	
			 stream = new ByteArrayInputStream(msg.getBytes("UTF_8"));
		
		updateResponse=new BufferedReader(new InputStreamReader(stream));
		response = (R)updateResponse;
//		response = (R)msg;
		
		listener.onFailure(response);
	}
	
	private String responseString(BufferedReader updateResponse)throws IOException{
		StringBuilder total = new StringBuilder();
		String line;
	
			while ((line =updateResponse.readLine()) != null) {
			    total.append(line);
			}
			return total.toString();
	}

}
