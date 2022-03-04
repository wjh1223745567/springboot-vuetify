package com.iotinall.framework.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 序列生成
 * @author WJH
 * @date 2019/12/1310:38
 */
public class CodeOrderUtil {

    public static final String tokenKey = "-token-";

    /**
     * 获取token
     * @param username
     * @return
     */
    public static String token(String username){
        return AesEncryptUtils.aesEncryptToken(username) + tokenKey + RandomStringUtils.randomAlphanumeric(10);
    }
}
