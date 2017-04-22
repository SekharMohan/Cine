package com.cine.service.network;

import android.os.AsyncTask;
import android.util.Log;

import com.cine.service.network.callback.ICallBack;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


public class WebServiceRequest<R extends Object> extends
		AsyncTask<Void, Void, Boolean> {

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
		GET, POST,POSTWITHBODY
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
	protected Boolean doInBackground(Void... parms)  {
		// TODO Auto-generated method stub
		HttpURLConnection result = null;
		try{
		RestService restService = new RestService();

		if (requestMethod == RequestMethod.GET) {

			if(params!=null){

			 result= restService.executeGetRequest(url+params.getParamList());
			}else{
				 result=restService.executeGetRequest(url);
			}

		}

		else {

			 result= restService.executePostResquest(url,params.getParamList());

		}
		return setResponse(result);
		}catch(Exception e){
			try {
				handleException(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		/*onerror*/
return false;

		}
		

	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result!=null){
			if(result){
				listener.onSuccess(response);
			}else{
				listener.onFailure(response);
			}
		}
	}

	
	@SuppressWarnings("unchecked")
	private boolean setResponse(HttpURLConnection result)throws IOException {
		// TODO Auto-generated method stub

		BufferedReader updateResponse;
		
		if(result.getResponseCode()==HttpURLConnection.HTTP_OK){
		Log.d("RestService-Response", result.getResponseCode()+":"+result.getResponseMessage());
		updateResponse=new BufferedReader(new InputStreamReader(result.getInputStream()));
		
		/*response = (R)updateResponse;
		listener.onSuccess(response);*/
		
		response = (R)responseString(updateResponse);
		return true;
		
		}
	else{
		Log.e("RestService-Error", result.getResponseCode()+":"+result.getResponseMessage());
		
		updateResponse=new BufferedReader(new InputStreamReader(result.getErrorStream()));
//		response = (R)updateResponse;
		response = (R)responseString(updateResponse);
return false;
		
	}
		
		

		
	}
	@SuppressWarnings("unchecked")
	private void handleException(Exception e)throws IOException{
		String msg=e.getMessage();
		/*BufferedReader updateResponse;
		InputStream stream = null;
	
			 stream = new ByteArrayInputStream(msg.getBytes("UTF_8"));
		
		updateResponse=new BufferedReader(new InputStreamReader(stream));
//		response = (R)updateResponse;
		*/response = (R)msg;
		

	}
	
	private String responseString(BufferedReader updateResponse)throws IOException{
		StringBuilder total = new StringBuilder();
		String line;
	
			while ((line =updateResponse.readLine()) != null) {
			    total.append(line);
				System.out.println(line);
			}
			return total.toString();
	}
	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
		return readFully(inputStream).toString(encoding);
	}

	private ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos;
	}

}
