package com.ucloud.ufile.auth;


/**
 * 远程授权者
 *
 * @author: joshua
 * @E-mail: joshua.yin@ucloud.cn
 * @date: 2018/11/6 18:50
 */
public abstract class ObjectRemoteAuthorization extends ObjectAuthorization {
    /**
     * 授权接口URL
     */
    protected String authorizationUrl;

    /**
     * 构造方法
     *
     * @param publicKey        用户公钥
     * @param authorizationUrl 授权接口URL
     */
    public ObjectRemoteAuthorization(String publicKey, String authorizationUrl) {
        super(publicKey);
        this.authorizationUrl = authorizationUrl;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }
}
