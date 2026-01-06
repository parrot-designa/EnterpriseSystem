package com.enterprisesystem.babycommon.utils;

public class CommonStringUtil {
    public static boolean isBlank(CharSequence str){
        return str == null || str.length() == 0;
    }
}
