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
            "Kf6owXtNZSimr0rR7w9ew6Iclm1w73QB8+jUkiV7hXgBYtV5BNWN1LlNUko=",
            "9K91tK7hcpCFL+90HwVk8lGUwJrqqjzfUouDD47RLrs9f1Umt4gXx7LWwB4kE7um");

    public static final UfileObjectLocalAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(
            "Kf6owXtNZSimr0rR7w9ew6Iclm1w73QB8+jUkiV7hXgBYtV5BNWN1LlNUko=",
            "9K91tK7hcpCFL+90HwVk8lGUwJrqqjzfUouDD47RLrs9f1Umt4gXx7LWwB4kE7um");
}
