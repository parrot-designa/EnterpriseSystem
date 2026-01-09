package com.enterprisesystem.babysecure.shiro.config;

import com.alibaba.fastjson.JSONObject;
import com.enterprisesystem.babysecure.shiro.loginchain.AccountChain;
import com.enterprisesystem.babysecure.shiro.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

@AllArgsConstructor
public class JwtRealm extends AuthorizingRealm{
    public static ThreadLocal<JSONObject> params = new ThreadLocal<>();

    public static JSONObject initParams(){
        JSONObject jsonObject = params.get();
        if(jsonObject == null){
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof JwtToken;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken loginToken = (JwtToken) token;
        // 获取 token
        String credentials = loginToken.getCredentials().toString();
        String account = (String) JwtUtil.parseJWT(credentials).get(Const.JWTTOKEN_KEY_ACCOUNT);
        String pwd = (String) JwtUtil.parseJWT(credentials).get(Const.JWTTOKEN_KEY_PWD);

        JSONObject jsonObject = initParams();
        jsonObject.put("account",account);
        jsonObject.put("pwd", pwd);
        jsonObject.put("token",credentials);
        params.set(jsonObject);

        AccountChain accountChain = new AccountChain();
        accountChain.next();

        return new SimpleAuthenticationInfo(credentials,credentials,getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){
        String account = (String) JwtUtil.parseJWT(principalCollection.toString()).get("account");
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo();
        return (AuthorizationInfo) authenticationInfo;
    }
}
