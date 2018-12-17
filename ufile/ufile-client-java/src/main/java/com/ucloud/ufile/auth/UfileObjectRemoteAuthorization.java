package com.ucloud.ufile.auth;

import com.ucloud.ufile.auth.sign.UfileSignatureException;
import com.ucloud.ufile.util.HttpMethod;

/**
 *  Ufile默认的远程签名生成器
 * @author: joshua
 * @E-mail: joshua.yin@ucloud.cn
 * @date: 2018/11/7 15:31
 */
public final class UfileObjectRemoteAuthorization extends ObjectRemoteAuthorization {

    /**
     * 构造方法
     *
     * @param publicKey        用户公钥
     * @param authorizationUrl 授权接口URL
     */
    public UfileObjectRemoteAuthorization(String publicKey, String authorizationUrl) {
        super(publicKey, authorizationUrl);
    }

    @Override
    public String authorization(HttpMethod method, String bucket, String keyName, String contentType, String contentMD5, String date)
            throws UfileAuthorizationException, UfileSignatureException {
        return null;
    }

    @Override
    public String authorizePrivateUrl(HttpMethod method, String bucket, String keyName, long expires)
            throws UfileAuthorizationException, UfileSignatureException {
        return null;
    }
}
