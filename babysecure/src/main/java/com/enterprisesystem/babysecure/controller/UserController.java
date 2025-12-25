package com.enterprisesystem.babysecure.controller;

import com.enterprisesystem.babycommon.entity.APIResult;
import com.enterprisesystem.babysecure.constants.SecureApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Collections;
import java.util.List;

@RequestMapping(SecureApiConstants.API_V1 + "/users")
@RestController
public class UserController {

    @GetMapping
    public APIResult<List<String>> queryUsers(){
        APIResult<List<String>> apiResult = new APIResult();
        return apiResult;
    }

    @PostMapping
    public void addUser(){

    }
}