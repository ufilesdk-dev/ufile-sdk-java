package com.ucloud.ufile.auth;

import com.ucloud.ufile.auth.sign.Signer;
import com.ucloud.ufile.auth.sign.UfileSignatureException;
import com.ucloud.ufile.util.HttpMethod;

/**
 *   Ufile默认的本地签名生成器
 * @author: joshua
 * @E-mail: joshua.yin@ucloud.cn
 * @date: 2018/11/7 15:31
 */
public final class UfileObjectLocalAuthorization extends ObjectLocalAuthorization {

    public UfileObjectLocalAuthorization(String publicKey, String privateKey) {
        super(publicKey, privateKey);
    }

    public UfileObjectLocalAuthorization(String publicKey, String privateKey, Signer signer) {
        super(publicKey, privateKey, signer);
    }

    @Override
    public String authorization(HttpMethod method, String bucket, String keyName, String contentType, String contentMD5, String date)
            throws UfileAuthorizationException, UfileSignatureException {
        if (method == null)
            throw new UfileAuthorizationException("Param 'method' can not be null!");
        if (bucket == null || bucket.length() == 0)
            throw new UfileAuthorizationException("Param 'bucket' can not be blank!");
        keyName = keyName == null ? "" : keyName;
        contentType = contentType == null ? "" : contentType;
        contentMD5 = contentMD5 == null ? "" : contentMD5;
        date = date == null ? "" : date;

        StringBuffer signData = new StringBuffer();
        signData.append(method.getName() + "\n");
        signData.append(contentMD5 + "\n");
        signData.append(contentType + "\n");
        signData.append(date + "\n");
        signData.append("/" + bucket);
        signData.append("/" + keyName);

        String signature = signer.signature(privateKey, signData.toString());

        return "UCloud " + publicKey + ":" + signature;
    }

    @Override
    public String authorizePrivateUrl(HttpMethod method, String bucket, String keyName, long expires)
            throws UfileAuthorizationException, UfileSignatureException {
        if (method == null)
            throw new UfileAuthorizationException("Param 'method' can not be null!");
        if (bucket == null || bucket.length() == 0)
            throw new UfileAuthorizationException("Param 'bucket' can not be blank!");
        if (keyName == null || keyName.length() == 0)
            throw new UfileAuthorizationException("Param 'which' can not be blank!");
        if (expires <= 0)
            throw new UfileAuthorizationException("Param 'expires' must be > 0!");

        String contentMD5 = "";
        String contentType = "";

        StringBuffer signData = new StringBuffer();
        signData.append(method.getName() + "\n");
        signData.append(contentMD5 + "\n");
        signData.append(contentType + "\n");
        signData.append(expires + "\n");
        signData.append("/" + bucket);
        signData.append("/" + keyName);

        return signer.signature(privateKey, signData.toString());
    }
}
