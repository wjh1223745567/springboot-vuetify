package com.iotinall.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AesEncryptUtils {

    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    public static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static String encryptKey;

    public static String tokenKey = "L4W!@3Pnf0cGjmy!";

    public static byte[] base64Decode(String base64Code){
        return Base64.decodeBase64(base64Code);
    }

    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    public static String aesEncrypt(String content) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * 加密token
     * @param content
     * @return
     * @throws Exception
     */
    public static String aesEncryptToken(String content){
        String result = null;
        try {
            result = base64Encode(aesEncryptToBytes(content, tokenKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    public static String aesDecrypt(String encryptStr){
        String str = "";
        try {
            str = aesDecryptByBytes(base64Decode(encryptStr), encryptKey);
        } catch (Exception e) {
            log.error("解密失败");
        }
        return str;
    }

    @Value("${spring.encrypt.key}")
    public void setEncryptKey(String encryptKey) {
        AesEncryptUtils.encryptKey = encryptKey;
    }
}
