package com.enterprisesystem.babymain.domain.manager;

import com.enterprisesystem.babymain.domain.SellerDomain;
import com.enterprisesystem.babymain.mapper.SellerMapper;

import javax.annotation.Resource;

public class SellerDomainManager {

    @Resource
    private SellerMapper sellerMapper;

    public SellerDomain createSeller(String code,String name){
        SellerDomain sellerDomain = new SellerDomain();
        sellerDomain.setCode(code);
        sellerDomain.setName(name);
        return sellerDomain;
    }

    public SellerDomain addSeller(SellerDomain sellerDomain){
        this.sellerMapper.insert(sellerDomain.get)
        return sellerDomain;
    }
}
