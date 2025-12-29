package com.enterprisesystem.babysecure.service.impl;

import com.enterprisesystem.babysecure.model.dto.UserDto;
import com.enterprisesystem.babysecure.service.UserService;
import org.springframework.stereotype.Service;
import com.enterprisesystem.babycommon.utils.*;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto addUser(UserDto userDto){
        return doAddUser(userDto);
    }

    public UserDto doAddUser(UserDto userDto){
        // 账号去除前后空格
        userDto.setAccount(CommonObjectUtil.isNull(userDto.getAccount()) ? null : userDto.getAccount().trim());
        return userDto;
    }

    public void add(){

    }
}
