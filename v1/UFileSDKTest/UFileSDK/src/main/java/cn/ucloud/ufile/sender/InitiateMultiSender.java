package cn.ucloud.ufile.sender;

import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class InitiateMultiSender implements Sender {
	
	public InitiateMultiSender() {
		
	}

	@Override
	public void makeAuth(UFileClient client, UFileRequest request) {	
		String httpMethod = "POST";
		String contentMD5 = request.getContentMD5();
		String contentType = "";	
		String date = request.getDate();
		String canonicalUCloudHeaders = client.spliceCanonicalHeaders(request);
		String canonicalResource = "/" + request.getBucketName() + "/" + request.getKey();
		String stringToSign =  httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" +
				 canonicalUCloudHeaders + canonicalResource;
		client.makeAuth(stringToSign, request);
	}

	@Override
	public UFileResponse send(UFileClient ufileClient, UFileRequest request) {
		String uri = "";
		try {
			uri = "http://" + request.getBucketName() + ufileClient.getUFileConfig().getProxySuffix()
					+ "/" + URIUtil.encodeWithinPath(request.getKey(), UTF8)
					+ "?" + URIUtil.encodeWithinQuery("uploads", UTF8);
		} catch (URIException e) {
			e.printStackTrace();
		}
		
		HttpPost postMethod = new HttpPost(uri);
	
		HttpEntity resEntity = null;
		try {
			Map<String, String> headers = request.getHeaders();
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
		            postMethod.setHeader(entry.getKey(), entry.getValue());
		        }
			}
			HttpResponse httpResponse = ufileClient.getHttpClient().execute(postMethod);
			
			resEntity = httpResponse.getEntity();
			UFileResponse ufileResponse = new UFileResponse();
			ufileResponse.setStatusLine(httpResponse.getStatusLine());
			ufileResponse.setHeaders(httpResponse.getAllHeaders());
			
			if (resEntity != null) {
				ufileResponse.setContentLength(resEntity.getContentLength());
				ufileResponse.setContent(resEntity.getContent());
			}
			return ufileResponse;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (resEntity != null && resEntity.getContent() != null) {
					resEntity.getContent().close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
}
