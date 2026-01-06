package com.enterprisesystem.babysecure.shiro.request;

import lombok.Data;

@Data
public class LoginRequest extends BaseRequest{
    // 密码
    private String password;
}
