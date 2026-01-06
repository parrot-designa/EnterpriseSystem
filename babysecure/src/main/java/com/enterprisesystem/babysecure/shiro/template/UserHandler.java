package com.enterprisesystem.babysecure.shiro.template;

import com.alibaba.fastjson.JSONObject;
import com.enterprisesystem.babysecure.shiro.config.Const;
import com.enterprisesystem.babysecure.shiro.config.JwtToken;
import com.enterprisesystem.babysecure.shiro.request.LoginRequest;
import com.enterprisesystem.babysecure.shiro.util.JwtUtil;

import java.util.Map;

public class UserHandler extends AbstractLoginProcesserTemplate{
    @Override
    Map<String,Object> doProcess(String businessName, Map<String,Object> resultMap, LoginRequest loginRequest){
        JwtToken token = this.generateToken(loginRequest.getAccount(),loginRequest.getPassword());
        // 获取 JWT Token 字符串
        resultMap.put(Const.LOGIN_RESULT_MAP_KEY_TOKEN, token.getCredentials().toString());
        return resultMap;
    }

    /**
     * 生成 JWT Token
     *
     * @param account 用户账号
     * @param password 用户密码（加密后的）
     * @return JwtToken 对象
     */
    private JwtToken generateToken(String account, String password){
        // 构造 JWT Payload（载荷）
        JSONObject json = new JSONObject();
        json.put(Const.JWTTOKEN_KEY_ACCOUNT, account);  // 用户账号
        json.put(Const.JWTTOKEN_KEY_PWD, password);      // 加密后的密码

        // 创建 JWT Token（type=0 表示默认类型）
        int type = 0;
        String jwt = JwtUtil.createJWT(json, type);

        // 使用新的构造函数：JwtToken(token, account)
        // 这样 principal 是账号，credentials 是 Token
        return new JwtToken(jwt, account);
    }
}


