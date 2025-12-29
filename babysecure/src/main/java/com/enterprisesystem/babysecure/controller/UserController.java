package com.enterprisesystem.babysecure.controller;

import com.enterprisesystem.babycommon.annotation.ApiExceptionHandler;
import com.enterprisesystem.babycommon.constant.ApiConstants;
import com.enterprisesystem.babycommon.entity.APIResult;
import com.enterprisesystem.babysecure.model.dto.UserDto;
import com.enterprisesystem.babysecure.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@RequestMapping(ApiConstants.API_V1 + "/users")
@RestController
public class UserController {

    @Resource
    UserService userService;

    @PostMapping
    @ApiExceptionHandler(apiId = 3)
    public APIResult<UserDto> addUsers(UserDto userDto){
        return new APIResult<>(userService.addUser(userDto));
    }
}