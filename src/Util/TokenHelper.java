package Util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

public class TokenHelper {

    private String accessKey;
    private SecretKeySpec secretKey;

    private TokenHelper(String accessKey, SecretKeySpec secretKeySpec) {
        this.accessKey = accessKey;
        this.secretKey = secretKeySpec;
    }

    public static TokenHelper create(String accessKey, String secretKey) {
        byte[] sk = utf8Bytes(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(sk, "HmacSHA1");
        return new TokenHelper(accessKey, secretKeySpec);
    }

    public String getToken(String bucket) {
        //token有效期为一个小时
        long deadline = System.currentTimeMillis() / 1000L + get1Hour();
        String s = jsonEncoder(bucket, String.valueOf(deadline));
        return this.signWithData(utf8Bytes(s));
    }

    private String signWithData(byte[] data) {
        String s = encodeToString(data);
        return this.sign(utf8Bytes(s)) + ":" + s;
    }

    private String sign(byte[] data) {
        Mac mac = this.createMac();
        String encodedSign = encodeToString(mac.doFinal(data));
        return this.accessKey + ":" + encodedSign;
    }


    private static String encodeToString(byte[] data) {
        return Base64.encodeToString(data, 10);
    }

    private static byte[] utf8Bytes(String data) {
        try {
            return data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private Mac createMac() {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(this.secretKey);
            return mac;
        } catch (GeneralSecurityException var3) {
            var3.printStackTrace();
            throw new IllegalArgumentException(var3);
        }
    }

    private Long get1Hour(){
        return 3600L;
    }

    private String jsonEncoder(String bucket, String deadline) {
        return "{\"scope\":\"" + bucket + "\",\"deadline\":" + deadline + "}";
    }
}
