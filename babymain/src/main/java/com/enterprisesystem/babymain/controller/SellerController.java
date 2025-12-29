package com.enterprisesystem.babymain.controller;

import com.enterprisesystem.babycommon.constant.ApiConstants;
import com.enterprisesystem.babymain.model.dto.SellerDto;
import com.enterprisesystem.babymain.model.dto.SellerPageRequest;
import com.enterprisesystem.babymain.model.response.PageResult;
import com.enterprisesystem.babymain.service.SellerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping(ApiConstants.API_V3 + "/seller")
@RestController
public class SellerController {

    @Resource
    private SellerService sellerService;

    @PostMapping
    public SellerDto addSeller(SellerDto sellerDto){
        SellerDto dto = new SellerDto();
        dto.setCode(sellerDto.getCode());
        dto.setName(sellerDto.getName());
        sellerService.addSeller(dto);
        return dto;
    }

    /**
     * 分页查询商家
     */
    @GetMapping
    public PageResult<SellerDto> querySeller(SellerPageRequest request) {
        return sellerService.querySeller(request);
    }
}
