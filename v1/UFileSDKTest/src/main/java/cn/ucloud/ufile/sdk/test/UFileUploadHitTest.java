package cn.ucloud.ufile.sdk.test;


import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileConfig;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;
import cn.ucloud.ufile.sender.UploadHitSender;
import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 秒传测试，秒传是指如果UFile系统中含有待上传文件，则瞬间完成上传
 *
 * @author york
 */
public class UFileUploadHitTest {
    private static UFileConfig uFileConfig = null;

    public static void main(String args[]) {
        String bucketName = "";
        String key = "";
        String filePath = "";
        String configPath = "";

        //加载配置项
        uFileConfig = new UFileConfig(configPath);

        UFileRequest request = new UFileRequest();
        request.setBucketName(bucketName);
        request.setKey(key);
        request.setFilePath(filePath);

        System.out.println("UploadHit Test BEGIN ...");
        UFileClient ufileClient = null;
        try {
            ufileClient = new UFileClient(uFileConfig);
            uploadHit(ufileClient, request);
        } finally {
            ufileClient.shutdown();
        }
        System.out.println("UploadHit Test END ...\n\n");
    }

    private static void uploadHit(UFileClient ufileClient, UFileRequest request) {
        UploadHitSender sender = new UploadHitSender();
        sender.makeAuth(ufileClient, request);

        UFileResponse response = sender.send(ufileClient, request);
        if (response != null) {

            System.out.println("status line: " + response.getStatusLine());

            Header[] headers = response.getHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println("header " + headers[i].getName() + " : " + headers[i].getValue());
            }

            System.out.println("body length: " + response.getContentLength());

            InputStream inputStream = response.getContent();
            if (inputStream != null) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String s = "";
                    while ((s = reader.readLine()) != null) {
                        System.out.println(s);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
