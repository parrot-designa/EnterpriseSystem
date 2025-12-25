package com.enterprisesystem.babysecure.service.impl;

import com.enterprisesystem.babysecure.dto.UserDto;
import com.enterprisesystem.babysecure.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto addUser(UserDto userDto){
        return doAddUser(userDto);
    }

    public UserDto doAddUser(UserDto userDto){

        return userDto;
    }

    public void add(){

    }
}
