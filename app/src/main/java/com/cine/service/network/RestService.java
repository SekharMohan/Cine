package com.cine.service.network;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestService {
	private static HttpURLConnection httpConn;

	public void disconnect() {
		if (httpConn != null) {
			httpConn.disconnect();
		}

	}

	RestService() {
	}

	public HttpURLConnection  executeGetRequest(String url)throws IOException {
		
			URL getUrl = new URL(url);
			httpConn = (HttpURLConnection) getUrl.openConnection();
			httpConn.setUseCaches(false);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(false);

			
		
		return httpConn;
	}

	public static void setHttpConn(HttpURLConnection httpConn) {
		RestService.httpConn = httpConn;
	}

	public HttpURLConnection executePostResquest(String url,String params)throws IOException {
	

			URL postUrl = new URL(url);
			httpConn = (HttpURLConnection) postUrl.openConnection();
			httpConn.setUseCaches(false);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter writer = new OutputStreamWriter(
					httpConn.getOutputStream());
			writer.write(params);
			writer.flush();
			    	

		
		return httpConn;
	}

	

}
