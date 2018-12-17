package com.ucloud.ufile.sample;

import com.ucloud.ufile.auth.UfileBucketLocalAuthorization;
import com.ucloud.ufile.auth.UfileObjectLocalAuthorization;

/**
 * @author: joshua
 * @E-mail: joshua.yin@ucloud.cn
 * @date: 2018-12-11 16:13
 */
public class Constants {
    public static final UfileBucketLocalAuthorization BUCKET_AUTHORIZER = new UfileBucketLocalAuthorization(
            "your publick key",
            "your private key");

    public static final UfileObjectLocalAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(
            "your publick key",
            "your private key");
}
