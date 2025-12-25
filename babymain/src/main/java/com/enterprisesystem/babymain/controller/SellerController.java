package com.enterprisesystem.babymain.controller;

import com.enterprisesystem.babycommon.constant.ApiConstants;
import com.enterprisesystem.babymain.service.SellerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping(ApiConstants.API_V3 + "/seller")
@RestController
public class SellerController {

    @Resource
    private SellerService sellerService;

    @PostMapping
    public void addSeller(){
    }
}
