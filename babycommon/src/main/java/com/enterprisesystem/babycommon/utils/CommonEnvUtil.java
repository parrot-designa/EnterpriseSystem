package com.enterprisesystem.babycommon.utils;

import com.enterprisesystem.babycommon.context.ApplicationContextProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.*;

public class CommonEnvUtil {
    public static String getPropFromEnv(String key){
        return getPropFromEnv(key, StringUtils.EMPTY);
    }

    public static String getPropFromEnv(String key, String defaultValue){
        // 如果 Spring容器没有初始化完成 返回默认空字符
        if(!ApplicationContextProvider.isInit()){
            return defaultValue;
        }
        Environment environment = ApplicationContextProvider.getBean(Environment.class);
        return environment.getProperty(key, defaultValue);
    }

    /**
     * 打印所有的环境变量、配置变量信息
     */
    public static void printAllEnv(){
        // 获取所有属性（包括环境变量、配置文件等）
        Map<String, String> allProps = CommonEnvUtil.getAllProperties();

        // 只获取操作系统环境变量
        Map<String, String> envVars = System.getenv();

        // 按需使用
        System.out.println("Total properties: " + allProps.size());
        allProps.forEach((key, value) -> {
            System.out.println(key + " = " + value);
        });
    }

    /**
     * 获取所有的环境变量属性
     * @return
     */
    public static Map<String, String> getAllProperties() {
        // 检查容器初始化
        if (!ApplicationContextProvider.isInit()) {
            return Collections.emptyMap();
        }

        // 获取 Environment 并转换为 ConfigurableEnvironment
        Environment environment = ApplicationContextProvider.getBean(Environment.class);
        if (!(environment instanceof ConfigurableEnvironment)) {
            return Collections.emptyMap();
        }

        ConfigurableEnvironment configEnv = (ConfigurableEnvironment) environment;
        MutablePropertySources propertySources = configEnv.getPropertySources();
        Map<String, String> allProps = new LinkedHashMap<>();

        // 遍历所有属性源
        for (PropertySource<?> propertySource : propertySources) {
            String sourceName = propertySource.getName();
            Object source = propertySource.getSource();

            if (source instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) source;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    allProps.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }

        return allProps;
    }

}
