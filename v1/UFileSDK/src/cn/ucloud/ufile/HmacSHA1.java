package cn.ucloud.ufile;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class HmacSHA1 {
    private static final String DEFAULT_ENCODING = "UTF-8"; 
    private static final String ALGORITHM = "HmacSHA1"; 
    private static final Object LOCK = new Object();
    private static Mac macInstance; 

    public String getAlgorithm() {
        return ALGORITHM;
    }

    public HmacSHA1(){
    }

    public String sign(String key, String data){
        try{
            byte[] signData = sign(
                    key.getBytes(DEFAULT_ENCODING),
                    data.getBytes(DEFAULT_ENCODING));

            return toBase64String(signData);
        }
        catch(UnsupportedEncodingException ex){
            throw new RuntimeException("Unsupported algorithm: " + DEFAULT_ENCODING);
        }
    }


    private byte[] sign(byte[] key, byte[] data){
        try{
            // Because Mac.getInstance(String) calls a synchronized method,
            // it could block on invoked concurrently.
            // SO use prototype pattern to improve perf.
            if (macInstance == null) {
                synchronized (LOCK) {
                    if (macInstance == null) {
                        macInstance = Mac.getInstance(ALGORITHM);
                    }
                }
            }

            Mac mac = null;
            try {
                mac = (Mac)macInstance.clone();
            } catch (CloneNotSupportedException e) {
                // If it is not clonable, create a new one.
                mac = Mac.getInstance(ALGORITHM);
            }
            mac.init(new SecretKeySpec(key, ALGORITHM));
            return mac.doFinal(data);
        }
        catch(NoSuchAlgorithmException ex){
            throw new RuntimeException("Unsupported algorithm: " + ALGORITHM);
        }
        catch(InvalidKeyException ex){
            throw new RuntimeException();
        }
    }
    
    public String toBase64String(byte[] binaryData){
        return new String(Base64.encodeBase64(binaryData));
    }
}