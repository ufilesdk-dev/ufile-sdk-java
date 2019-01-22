package cn.ucloud.ufile.sender;

import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BasicHttpEntity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

public class UploadPartSender implements Sender {

    private String uploadId;
    private int partNumber;
    private long start;
    private long size;


    public UploadPartSender(String uploadId, int partNumber, long start,
                            long size) {
        this.uploadId = uploadId;
        this.partNumber = partNumber;
        this.start = start;
        this.size = size;
    }

    @Override
    public void makeAuth(UFileClient client, UFileRequest request) {
        String httpMethod = "PUT";
        String contentMD5 = request.getContentMD5();
        String contentType = request.getContentType();
        String date = request.getDate();
        String canonicalUCloudHeaders = client.spliceCanonicalHeaders(request);
        String canonicalResource = "/" + request.getBucketName() + "/" + request.getKey();
        String stringToSign = httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" +
                canonicalUCloudHeaders + canonicalResource;
        client.makeAuth(stringToSign, request);
    }

    @Override
    public UFileResponse send(UFileClient ufileClient, UFileRequest request) {
        String uri = "";
        try {
            uri = "http://" + request.getBucketName() + ufileClient.getUFileConfig().getProxySuffix()
                    + "/" + URIUtil.encodeWithinPath(request.getKey(), UTF8)
                    + "?" + URIUtil.encodeWithinQuery("uploadId", UTF8)
                    + "=" + URIUtil.encodeWithinQuery(this.uploadId, UTF8)
                    + "&" + URIUtil.encodeWithinQuery("partNumber", UTF8)
                    + "=" + URIUtil.encodeWithinQuery("" + this.partNumber, UTF8);
            System.out.println("[uri]: " + uri);
        } catch (URIException e) {
            e.printStackTrace();
        }
        HttpPut putMethod = new HttpPut(uri);

        HttpEntity resEntity = null;
        try {
            Map<String, String> headers = request.getHeaders();
            if (headers != null) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    putMethod.setHeader(entry.getKey(), entry.getValue());
                }
            }

            BasicHttpEntity reqEntity = new BasicHttpEntity();
            InputStream inputStream = new FileInputStream(request.getFilePath());
            inputStream.skip(start);
            reqEntity.setContent(inputStream);
            reqEntity.setContentLength(size);

            putMethod.setEntity(reqEntity);
            UFileResponse ufileResponse = new UFileResponse();
            HttpResponse httpResponse = ufileClient.getHttpClient().execute(putMethod);

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
