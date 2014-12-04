package br.com.coddy.cloudandpaste.utils;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class HttpUtils {

	private static int doPost(String url, JSONObject jsonContent) {

		DefaultHttpClient client = new DefaultHttpClient();

		HttpPost request = new HttpPost(url);
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept-Encoding", "gzip, deflate");
		
		StringEntity input = null;
		try {
			input = new StringEntity(jsonContent.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			return HttpStatus.SC_INTERNAL_SERVER_ERROR;
		}
		request.setEntity(input);

		HttpResponse response = null;
		try {
			response = client.execute(request);
			EntityUtils.toString(response.getEntity());
			return response.getStatusLine().getStatusCode();
		} catch (Throwable t1) {
			Log.e(HttpUtils.class.toString(), t1.getLocalizedMessage());
 		} finally {
			try {
				if (response != null) {
					response.getEntity().getContent().close();
				}
			} catch (Throwable t2) {
				// Do nothing
			}
		}
		
		return HttpStatus.SC_INTERNAL_SERVER_ERROR;
	}
	
	public static void doPostAsync(final String url, 
			final JSONObject json, final HttpCallback callback) {
		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected Integer doInBackground(Void... params) {
				return doPost(url, json);
			};
			
			@Override
			protected void onPostExecute(Integer result) {
				callback.done(result);
			}
		}.execute();
	}
	
}
