package cn.ucloud.ufile.sender;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileConfig;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;

public class PostSender implements Sender {
	
	public PostSender() {
		
	}

	@Override
	public void makeAuth(UFileClient client, UFileRequest request) {		
		String httpMethod = "POST";
	    String contentMD5 = request.getContentMD5();
		String contentType = request.getContentType();	
		String date = request.getDate();
		String canonicalizedUcloudHeaders = client.spliceCanonicalHeaders(request);
		String canonicalized_resource = "/" + request.getBucketName() + "/" + request.getKey();
		String stringToSign =  httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" +
				 canonicalizedUcloudHeaders + canonicalized_resource;		
		client.makeAuth(stringToSign, request);
	}

	@Override
	public UFileResponse send(UFileClient ufileClient, UFileRequest request) {
		String uri = "http://" + request.getBucketName() + UFileConfig.getInstance().getProxySuffix() + "/";	
		HttpPost postMethod = new HttpPost(uri);
	
		HttpEntity resEntity = null;
		try {
			File file = new File(request.getFilePath());
			Map<String, String> headers = request.getHeaders();
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
		            postMethod.setHeader(entry.getKey(), entry.getValue());
		        }
			}
			
			ContentType requestContentType = ContentType.create(request.getContentType());
			FileBody fileBody = new FileBody(file, requestContentType, file.getName());
			ContentType textContentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
			
			HttpEntity entity = MultipartEntityBuilder.create()
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addPart("Authorization", new StringBody(request.getAuthorization(), textContentType))
					.addPart("FileName", new StringBody(request.getKey(), textContentType))
					.addPart("file", fileBody)
					.build();
			
			postMethod.setEntity(entity);
			postMethod.setHeader(entity.getContentType());
			
			UFileResponse ufileResponse = new UFileResponse();
			HttpResponse httpResponse = ufileClient.getHttpClient().execute(postMethod);
			
			resEntity = httpResponse.getEntity();

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
