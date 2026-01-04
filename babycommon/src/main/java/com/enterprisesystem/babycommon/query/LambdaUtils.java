package com.enterprisesystem.babycommon.query;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * Lambda 表达式工具类，用于提取字段名
 *
 * @author Claude Code
 * @date 2026-01-04
 */
public class LambdaUtils {

    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";

    /**
     * 根据 Lambda 表达式获取字段名（驼峰命名）
     *
     * @param function Lambda 表达式
     * @param <T>      实体类类型
     * @return 字段名（驼峰命名）
     */
    public static <T> String getFieldName(SerializableFunction<T, ?> function) {
        return getFieldName(function, false);
    }

    /**
     * 根据 Lambda 表达式获取字段名
     *
     * @param function      Lambda 表达式
     * @param toUnderline   是否转换为下划线命名
     * @param <T>           实体类类型
     * @return 字段名
     */
    public static <T> String getFieldName(SerializableFunction<T, ?> function, boolean toUnderline) {
        try {
            Method method = function.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(function);
            String methodName = lambda.getImplMethodName();
            String fieldName = methodNameToFieldName(methodName);
            return toUnderline ? camelToUnderscore(fieldName) : fieldName;
        } catch (Exception e) {
            throw new RuntimeException("无法从 Lambda 表达式提取字段名", e);
        }
    }

    /**
     * 将方法名转换为字段名
     *
     * @param methodName 方法名（如：getUserName 或 isActive）
     * @return 字段名（如：userName 或 active）
     */
    private static String methodNameToFieldName(String methodName) {
        if (methodName.startsWith(GET_PREFIX)) {
            methodName = methodName.substring(GET_PREFIX.length());
        } else if (methodName.startsWith(IS_PREFIX)) {
            methodName = methodName.substring(IS_PREFIX.length());
        }
        // 首字母小写
        return Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
    }

    /**
     * 驼峰命名转下划线命名
     *
     * @param camelCase 驼峰命名（如：userName）
     * @return 下划线命名（如：user_name）
     */
    public static String camelToUnderscore(String camelCase) {
        if (StringUtils.isBlank(camelCase)) {
            return "";
        }
        StringBuilder underscore = new StringBuilder();
        char[] chars = camelCase.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                underscore.append('_').append(Character.toLowerCase(c));
            } else {
                underscore.append(c);
            }
        }
        return underscore.toString();
    }

    /**
     * 下划线命名转驼峰命名
     *
     * @param underscore 下划线命名（如：user_name）
     * @return 驼峰命名（如：userName）
     */
    public static String underscoreToCamel(String underscore) {
        if (StringUtils.isBlank(underscore)) {
            return "";
        }
        StringBuilder camel = new StringBuilder();
        String[] parts = underscore.split("_");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (StringUtils.isNotBlank(part)) {
                if (i == 0) {
                    camel.append(part.toLowerCase());
                } else {
                    camel.append(Character.toUpperCase(part.charAt(0)))
                            .append(part.substring(1).toLowerCase());
                }
            }
        }
        return camel.toString();
    }
}
