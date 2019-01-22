package cn.ucloud.ufile.sdk.test;


import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileConfig;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;
import cn.ucloud.ufile.sender.DeleteSender;
import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 删除文件测试
 *
 * @author york
 */
public class UFileDeleteTest {
    private static UFileConfig uFileConfig = null;

    public static void main(String args[]) {
        String bucketName = "";
        String key = "";
        String configPath = "";

        //加载配置项
        uFileConfig = new UFileConfig(configPath);

        UFileRequest request = new UFileRequest();
        request.setBucketName(bucketName);
        request.setKey(key);

        UFileClient ufileClient = null;
        try {
            ufileClient = new UFileClient(uFileConfig);
            deleteFile(ufileClient, request);
        } finally {
            ufileClient.shutdown();
        }
    }

    private static void deleteFile(UFileClient ufileClient, UFileRequest request) {

        DeleteSender sender = new DeleteSender();
        sender.makeAuth(ufileClient, request);

        UFileResponse response = sender.send(ufileClient, request);
        if (response != null) {
            System.out.println("status line: " + response.getStatusLine());
            Header[] headers = response.getHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println("header " + headers[i].getName() + " : " + headers[i].getValue());
            }

            System.out.println("body length: " + response.getContentLength());

            if (response.getContent() != null) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(response.getContent()));
                    String input;
                    while ((input = br.readLine()) != null) {
                        System.out.println(input);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
