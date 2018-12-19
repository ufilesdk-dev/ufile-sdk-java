package cn.ucloud.ufile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.client.HttpClient;

import cn.ucloud.ufile.body.ErrorBody;

import com.google.gson.Gson;
import org.apache.http.impl.client.DefaultHttpClient;

public class UFileClient {
	private static final String CANONICAL_PREFIX = "X-UCloud";
	private HttpClient httpClient;
	
	
	public UFileClient() {
		httpClient = new DefaultHttpClient();
		this.setHttpClient(httpClient);
	}

	public void makeAuth(String stringToSign, UFileRequest request) {
		String signature = new HmacSHA1().sign(UFileConfig.getInstance().getUcloudPrivateKey(), stringToSign);
		String authorization = "UCloud" + " " + UFileConfig.getInstance().getUcloudPublicKey() + ":" + signature;
		request.setAuthorization(authorization);
	}


	public String spliceCanonicalHeaders(UFileRequest request) {
		Map<String, String> headers = request.getHeaders();
	    Map<String, String> sortedMap = new TreeMap<String, String>();
		
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				if (entry.getKey().startsWith(CANONICAL_PREFIX)) {
					sortedMap.put(entry.getKey().toLowerCase(), entry.getValue());
				}
	        }
			String result = "";
			for (Entry<String, String> entry : sortedMap.entrySet()) {
				result += entry.getKey() + ":" +  entry.getValue() + "\n";
	        }
			return result;
		} else {
			return "";
		}
	}


	public void closeErrorResponse(UFileResponse res) {
		InputStream inputStream = res.getContent();
		if (inputStream != null) {
			Reader reader = new InputStreamReader(inputStream);
			Gson gson = new Gson();
			ErrorBody body = gson.fromJson(reader, ErrorBody.class);
			String bodyJson = gson.toJson(body);
			System.out.println(bodyJson);
		
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}


	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void shutdown() {
		httpClient.getConnectionManager().shutdown();
	}
}
