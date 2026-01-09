package com.enterprisesystem.babysecure.shiro.loginchain;

import com.alibaba.fastjson.JSONObject;
import com.enterprisesystem.babycommon.context.ApplicationContextProvider;
import com.enterprisesystem.babycommon.exception.SystemRuntimeException;
import com.enterprisesystem.babysecure.mapper.UserMapper;
import com.enterprisesystem.babysecure.model.entity.UserEntity;
import com.enterprisesystem.babysecure.shiro.config.JwtRealm;
import com.enterprisesystem.babysecure.shiro.entity.LoginErrorResult;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;


/**
 * 登录责任链 用于分级查验登录错误
 */
@Data
public abstract class AbstractLoginChain {
    AbstractLoginChain nextChain;

    abstract LoginErrorResult goChain();

    public void next(){
        LoginErrorResult error = this.goChain();
        if(error != null){
            SystemRuntimeException exception = new SystemRuntimeException(error.getErrorCode(),error.getErrorMsg());
            throw new AuthenticationException(exception);
        }
        if(nextChain != null){
            nextChain.next();
        }
    }

    /**
     * 根据请求参数中的 account 查询用户
     *
     * @return 用户实体对象，如果账号不存在返回 null
     */
    public UserEntity getUser(){
        // 1. 从 ThreadLocal 中获取登录参数
        JSONObject jsonObject = JwtRealm.params.get();

        if (jsonObject == null) {
            return null;
        }
        UserEntity user = (UserEntity) jsonObject.get("user");

        if(user == null){
            // 2. 提取账号
            String account = (String) jsonObject.get("account");
            if (account == null || account.trim().isEmpty()) {
                return null;
            }

            // 3. 从 Spring 容器获取 UserMapper
            UserMapper userMapper = ApplicationContextProvider.getBean(UserMapper.class);
            user = userMapper.selectByAccount(account);
            // 4. 根据账号查询用户
            jsonObject.put("user",user);
        }

        return user;
    }
}
