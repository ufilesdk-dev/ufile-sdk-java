package com.ucloud.ufile.auth;

import com.ucloud.ufile.auth.sign.UfileSignatureException;
import com.ucloud.ufile.util.HttpMethod;

import java.util.Map;

/**
 * 对象存储相关API授权者
 *
 * @author: joshua
 * @E-mail: joshua.yin@ucloud.cn
 * @date: 2018/11/12 16:27
 */
public interface ObjectAuthorizer extends Authorizer {

    /**
     * 获取授权签名
     *
     * @param method      Http请求方式 {@link HttpMethod}
     * @param bucket      bucket名称
     * @param keyName     文件keyName
     * @param contentType 文件mimeType
     * @param contentMD5  文件MD5
     * @param date        日期（yyyyMMddHHmmss）
     * @return 授权签名
     * @throws UfileAuthorizationException 授权异常时抛出
     * @throws UfileSignatureException     签名异常时抛出
     */
    String authorization(HttpMethod method, String bucket, String keyName, String contentType, String contentMD5, String date)
            throws UfileAuthorizationException, UfileSignatureException;

    /**
     * 获取私有仓库URL签名
     *
     * @param method  Http请求方式 {@link HttpMethod}
     * @param bucket  bucket名称
     * @param keyName 文件keyName
     * @param expires 过期时间 (当前时间加上一个有效时间, 单位：Unix time second)
     * @return 私有仓库URL签名
     * @throws UfileAuthorizationException 授权异常时抛出
     * @throws UfileSignatureException     签名异常时抛出
     */
    String authorizePrivateUrl(HttpMethod method, String bucket, String keyName, long expires)
            throws UfileAuthorizationException, UfileSignatureException;
}
