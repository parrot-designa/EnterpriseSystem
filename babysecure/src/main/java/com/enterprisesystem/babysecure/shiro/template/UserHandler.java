package com.enterprisesystem.babysecure.shiro.template;

import com.alibaba.fastjson.JSONObject;
import com.enterprisesystem.babycommon.exception.SystemRuntimeException;
import com.enterprisesystem.babysecure.mapper.UserMapper;
import com.enterprisesystem.babysecure.model.entity.UserEntity;
import com.enterprisesystem.babysecure.service.UserService;
import com.enterprisesystem.babysecure.shiro.config.Const;
import com.enterprisesystem.babysecure.shiro.config.JwtToken;
import com.enterprisesystem.babysecure.shiro.request.LoginRequest;
import com.enterprisesystem.babysecure.shiro.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class UserHandler extends AbstractLoginProcesserTemplate{

    @Autowired
    UserMapper userMapper;

    @Override
    Map<String,Object> doProcess(String businessName, Map<String,Object> resultMap, LoginRequest loginRequest){
        // 1. 查询数据库中是否存在该用户
        UserEntity user = userMapper.selectByAccount(loginRequest.getAccount());

        if (user == null) {
            // 用户不存在
            throw new SystemRuntimeException(102121, "用户不存在");
        }

        // 2. 验证密码（可选）
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            // 密码错误
            throw new SystemRuntimeException(102122, "账号密码不正确");
        }

        // 4. 生成 JWT Token
        JwtToken token = this.generateToken(loginRequest.getAccount(), loginRequest.getPassword());
        resultMap.put(Const.LOGIN_RESULT_MAP_KEY_TOKEN, token.getCredentials().toString());

        // 5. 执行登录认证
        try{
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        }catch (Exception e){
            throw new SystemRuntimeException(102122, "登录失败");
        }

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


