package cn.ucloud.ufile.sdk.test;


import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileConfig;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;
import cn.ucloud.ufile.sender.OptionsSender;
import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HeadFile请求测试
 *
 * @author york
 */
public class UFileOptionsTest {
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

        request.addHeader("Access-Control-Reuquest-Method", "POST");
        request.addHeader("Origin", "foo");

        UFileClient ufileClient = null;

        try {
            ufileClient = new UFileClient(uFileConfig);
            optionsFile(ufileClient, request);
        } finally {
            ufileClient.shutdown();
        }
    }

    private static void optionsFile(UFileClient ufileClient, UFileRequest request) {
        OptionsSender sender = new OptionsSender();
        sender.makeAuth(ufileClient, request);

        UFileResponse response = sender.send(ufileClient, request);
        if (response != null) {

            System.out.println("status line: " + response.getStatusLine());

            Header[] headers = response.getHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println("header " + headers[i].getName() + " : " + headers[i].getValue());
            }

            System.out.println("body length: " + response.getContentLength());

            /*对于Head请求，如果响应的Status是 200 OK，则响应中的body部分为0。
             * 但如果遇到错误，响应中的body部分可能不为空
             */
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
                    if (response.getContent() != null) {
                        try {
                            response.getContent().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
