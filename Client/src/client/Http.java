package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

import json.Bundles;

/**
 * 
 * @author jack
 *
 */
public class Http {
	private static CloseableHttpClient client = HttpClients.createDefault();
	private static Gson gson = new Gson();

	public static String doPost(String url, Object json) throws ClientProtocolException, IOException{
		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(gson.toJson(json)));
		post.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = client.execute(post);
		String body = IOUtils.toString(response.getEntity().getContent());
		return body;
	}
	public static String doGet(String url) throws UnsupportedOperationException, IOException{
		HttpGet get = new HttpGet(url);
		HttpResponse res = client.execute(get);
		String body = IOUtils.toString(res.getEntity().getContent());
		return body;
	}
	
}
