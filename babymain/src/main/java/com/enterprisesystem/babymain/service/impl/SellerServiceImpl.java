package com.enterprisesystem.babymain.service.impl;

import com.enterprisesystem.babymain.model.dto.SellerDto;
import com.enterprisesystem.babymain.mapper.SellerMapper;
import com.enterprisesystem.babymain.service.SellerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private SellerMapper sellerMapper;

    @Override
    public SellerDto addSeller(SellerDto sellerDto) {
        sellerMapper.insert(sellerDto);
        return sellerDto;
    }
}
