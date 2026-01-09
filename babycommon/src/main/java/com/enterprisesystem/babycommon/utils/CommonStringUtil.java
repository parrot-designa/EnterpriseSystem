package com.enterprisesystem.babycommon.utils;

public class CommonStringUtil {
    public static boolean isBlank(CharSequence str){
        return str == null || str.length() == 0;
    }

    public static boolean isNotBlank(CharSequence str){
        return !isBlank(str);
    }
}
