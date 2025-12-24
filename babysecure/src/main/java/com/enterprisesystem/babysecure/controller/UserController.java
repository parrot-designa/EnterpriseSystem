package com.enterprisesystem.babysecure.controller;

//import com.enterprisesystem.babysecure.constant.SecureApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

//@RequestMapping(SecureApiConstants.API_V1 + "/users")
@RestController
public class UserController {
    @GetMapping("/user")
    public String queryUser(){
        return "我帮你查询出来很多用户！";
    }
}