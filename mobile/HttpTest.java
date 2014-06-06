package com.extra.crazyguess.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;
import android.util.Log;


public class HttpTest {

	@Test
	public void sendData() {

		HttpClient client = AndroidHttpClient.newInstance("");

		try {
			BasicHttpContext context = new BasicHttpContext();
			context.setAttribute(ClientContext.COOKIE_STORE,
					new BasicCookieStore());
			HttpPost httppost = new HttpPost("http://115.239.211.110/paiser");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("data", "userDatas.toString()"));
			httppost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			HttpResponse response = client.execute(httppost, context);
			HttpEntity entity = response.getEntity();
			Log.e("hcy", this.toString(entity.getContent()));
			entity.consumeContent();
		} catch (IOException e) {
			Log.e("hcy", " "+e.getMessage());
		}
	}

	private String toString(InputStream is) throws IOException {
		String ret = "";
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String tmp = br.readLine();
		while (tmp != null) {
			ret += tmp;
			tmp = br.readLine();
		}
		br.close();
		return ret;
	}

}
