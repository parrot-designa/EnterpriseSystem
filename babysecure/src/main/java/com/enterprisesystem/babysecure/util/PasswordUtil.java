package com.enterprisesystem.babysecure.util;

import com.enterprisesystem.babycommon.exception.SystemRuntimeException;

import java.util.Base64;

public class PasswordUtil {

    private static final String PREFIX = "baby";
    private static final String SUFFIX = "go";

    public static String decrypt(String encryptedPassword){
        String pwd;
        // 调用 base64
        String decryptString = new String(Base64.getDecoder().decode(encryptedPassword));

        if(decryptString.startsWith(PREFIX) && decryptString.endsWith(SUFFIX)){
            int startIndex = 4;
            int endIndex = decryptString.length() - 2;
            pwd = decryptString.substring(startIndex, endIndex);
        }
        else {
            throw new SystemRuntimeException(100098, "无法解析该密码" + encryptedPassword);
        }

        return pwd;
    }
}
