package com.enterprisesystem.babysecure.controller.auth;

import com.enterprisesystem.babycommon.annotation.ApiExceptionHandler;
import com.enterprisesystem.babycommon.constant.ApiConstants;
import com.enterprisesystem.babycommon.entity.APIResult;
import com.enterprisesystem.babysecure.shiro.action.UserShiroFacade;
import com.enterprisesystem.babysecure.shiro.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(ApiConstants.API_V1 + "/login")
public class LoginController {

    @Autowired
    private UserShiroFacade shiroFacade;

    @PostMapping("/user-login")
    @ApiExceptionHandler(apiId = 10086)
    public APIResult<Map<String,Object>> login(@RequestBody LoginRequest loginRequest){
        APIResult<Map<String,Object>> mapAPIResult = new APIResult<>();
        Map<String,Object> map = shiroFacade.doLogin(loginRequest);
        mapAPIResult.setSuccess(true);
        mapAPIResult.setData(map);
        return mapAPIResult;
    }
}
