package com.ucloud.ufile.sample.object;

import com.ucloud.ufile.UfileClient;
import com.ucloud.ufile.api.ApiError;
import com.ucloud.ufile.api.object.ObjectConfig;
import com.ucloud.ufile.bean.ObjectListBean;
import com.ucloud.ufile.bean.UfileErrorBean;
import com.ucloud.ufile.http.UfileCallback;
import com.ucloud.ufile.sample.Constants;
import com.ucloud.ufile.util.JLog;
import okhttp3.Request;

/**
 *
 * @author: joshua
 * @E-mail: joshua.yin@ucloud.cn
 * @date: 2018-12-11 14:32
 */
public class ObjectListSample {
    private static final String TAG = "ObjectListSample";
    private static ObjectConfig config = new ObjectConfig("your bucket region", "ufileos.com");

    public static void main(String[] args) {
        String bucketName = "bucketName";

        UfileClient.object(Constants.OBJECT_AUTHORIZER, config)
                .objectList(bucketName)
                /**
                 * 过滤前缀
                 */
//                .withPrefix("")
                /**
                 * 分页标记
                 */
//                .withMarker("")
                /**
                 * 分页数据上限，Default = 20
                 */
//                .dataLimit(10)
                .executeAsync(new UfileCallback<ObjectListBean>() {
                    @Override
                    public void onProgress(long bytesWritten, long contentLength) {
                        JLog.D(TAG, String.format("[progress] = %d%% - [%d/%d]", (int) (bytesWritten * 1.f / contentLength * 100), bytesWritten, contentLength));
                    }

                    @Override
                    public void onResponse(ObjectListBean response) {
                        JLog.D(TAG, String.format("[res] = %s", (response == null ? "null" : response.toString())));
                    }

                    @Override
                    public void onError(Request request, ApiError error, UfileErrorBean response) {
                        JLog.D(TAG, String.format("[error] = %s\n[info] = %s",
                                (error == null ? "null" : error.toString()),
                                (response == null ? "null" : response.toString())));
                    }
                });
    }
}
