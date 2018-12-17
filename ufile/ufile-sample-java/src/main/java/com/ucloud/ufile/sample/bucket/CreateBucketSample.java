package com.ucloud.ufile.sample.bucket;

import com.ucloud.ufile.UfileClient;
import com.ucloud.ufile.api.ApiError;
import com.ucloud.ufile.api.bucket.BucketType;
import com.ucloud.ufile.bean.BucketResponse;
import com.ucloud.ufile.bean.UfileErrorBean;
import com.ucloud.ufile.exception.UfileException;
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
public class CreateBucketSample {
    private static final String TAG = "CreateBucketSample";

    public static void main(String[] args) {
        String bucketName = "bucketName";
        String region = "region";
        BucketType bucketType = BucketType.PRIVATE;

        execute(bucketName, region, bucketType);
    }

    public static void execute(String bucketName, String region, BucketType bucketType) {
        try {
            BucketResponse res = UfileClient.bucket(Constants.BUCKET_AUTHORIZER)
                    .createBucket(bucketName, region, bucketType)
                    .execute();
            JLog.D(TAG, String.format("[res] = %s", (res == null ? "null" : res.toString())));
        } catch (UfileException e) {
            e.printStackTrace();
        }
    }

    public static void executeAsync(String bucketName, String region, BucketType bucketType) {
        UfileClient.bucket(Constants.BUCKET_AUTHORIZER)
                .createBucket(bucketName, region, bucketType)
                .executeAsync(new UfileCallback<BucketResponse>() {
                    @Override
                    public void onResponse(BucketResponse response) {
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
