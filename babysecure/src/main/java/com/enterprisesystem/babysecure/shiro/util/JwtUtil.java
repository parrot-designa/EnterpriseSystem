package com.enterprisesystem.babysecure.shiro.util;

import com.enterprisesystem.babycommon.exception.SystemRuntimeException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 * 使用 jjwt 0.11.5 版本
 */
public class JwtUtil {

    /**
     * JWT生成密钥使用的密码
     */
    private static final String JWT_RULE = "BABY_SSO_JWT_PWD";

    /**
     * JWT添加至 HTTP HEAD中的前缀
     */
    private static final String JWT_SEPARATOR = "BABY_SSO_JWT";

    /**
     * JWT签名算法 (HMAC-SHA256)
     * 注意：jjwt 0.11.x 中算法由密钥类型决定，这里仅作注释说明
     */
    private static final String JWT_ALGORITHM = "HmacSHA256";

    public static String createJWT(Map<String, Object> map, int type){
        return createJWT(map, -1, type);
    }

    private static String createJWT(Map<String,Object> map, long time, int type){
        // 生成 JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 获取加密密钥
        SecretKey key = generalKey();

        // 获取 JWT字符串
        JwtBuilder builder = Jwts.builder()
                .setClaims(map)
                .setIssuedAt(now)
                .signWith(key); // 0.11.x 版本不再需要指定算法，算法由密钥类型决定

        if(time >= 0){
            builder.setExpiration(new Date((nowMillis + time)));
        }

        return JWT_SEPARATOR + builder.compact();
    }

    /**
     * 生成 JWT 密钥
     * 使用 HmacSHA256 算法
     */
    public static SecretKey generalKey(){
        // 将密钥生成转换为字节数组
        byte[] bytes = JWT_RULE.getBytes(StandardCharsets.UTF_8);
        // 使用 HmacSHA256 算法生成密钥
        return new SecretKeySpec(bytes, JWT_ALGORITHM);
    }

    /**
     * 生成 JWT 密钥（自定义密钥规则）
     * @param rule 密钥字符串
     */
    public static SecretKey generalKey(String rule){
        byte[] bytes = rule.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(bytes, JWT_ALGORITHM);
    }

    /**
     * 解析 JWT（使用默认密钥）
     * @param jsonWebToken JWT字符串
     * @return Claims 对象
     */
    public static Claims parseJWT(String jsonWebToken){
        SecretKey key = generalKey();
        return parseJWT(jsonWebToken, key);
    }

    /**
     * JWT解密
     * @param jsonWebToken JWT字符串
     * @param key 加密密钥
     * @return Claims 对象
     */
    public static Claims parseJWT(String jsonWebToken, Key key){
        Claims claims = null;
        try{
            // 移除 JWT前缀字符串
            String clamsJwt = StringUtils.substringAfter(jsonWebToken, JWT_SEPARATOR);

            // 使用 parserBuilder() 而不是 parser() (0.11.x 版本变化)
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(clamsJwt)
                    .getBody();
        }catch(Exception e){
            throw new SystemRuntimeException(11221, "JWT解析异常");
        }
        return claims;
    }

}
