	package com.cine.service.network;

	import android.util.Log;

	import java.io.BufferedWriter;
	import java.io.IOException;
	import java.io.OutputStream;
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

		public HttpURLConnection executePostResquest(String url,String params,String contentType)throws IOException {

			Log.d("URL---->",url+params);
				URL postUrl = new URL(url);

				httpConn = (HttpURLConnection) postUrl.openConnection();
			httpConn.setUseCaches(false);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.setReadTimeout(10000 /* milliseconds */);
			httpConn.setConnectTimeout(15000 /* milliseconds */);
			httpConn.setRequestProperty("User-Agent", "android");
			httpConn.setRequestProperty("Accept", "application/json");
			httpConn.setRequestProperty("Content-Type", contentType);
			httpConn.connect();

		OutputStream os = httpConn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			writer.write(params);
			writer.flush();
			writer.close();
	os.close();

			return httpConn;
		}



	}
