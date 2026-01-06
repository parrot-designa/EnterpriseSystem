package com.enterprisesystem.babysecure.shiro.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * JWT加密类型
     */
    private static final SignatureAlgorithm JWT_ALG = SignatureAlgorithm.HS256;

    /**
     * JWT生成密钥使用的密码
     */
    private static final String JWT_RULE = "BABY_SSO_JWT_PWD";
    /**
     * JWT添加至 HTTP HEAD中的前缀
     */
    private static final String JWT_SEPARATOR = "BABY_SSO_JWT";

    public static String createJWT(Map<String, Object> map, int type){
        return createJWT(map, -1, type);
    }

    private static String createJWT(Map<String,Object> map,long time,int type){
        return createJWT(map,time, JWT_ALG, type);
    }

    public static String createJWT(Map<String,Object> map,long time,SignatureAlgorithm alg,int type){
        // 生成 JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 获取加密密钥
        SecretKey key = generalKey(alg);
        // 获取 JWT字符串
        JwtBuilder builder = Jwts.builder().setClaims(map).setIssuedAt(now).signWith(alg,key);
        if(time >= 0){
            builder.setExpiration(new Date((nowMillis + time)));
        }
        return JWT_SEPARATOR + builder.compact();
    }

    public static SecretKey generalKey(SignatureAlgorithm alg){
        return generalKey(JWT_ALG, JWT_RULE);
    }

    public static SecretKey generalKey(SignatureAlgorithm alg,String rule){
        // 将密钥生成转换为字节数组
        byte[] bytes = rule.getBytes(StandardCharsets.UTF_8);
        // 根据指定的加密方式，生成密钥
        return new SecretKeySpec(bytes, alg.getFamilyName());
    }
}
