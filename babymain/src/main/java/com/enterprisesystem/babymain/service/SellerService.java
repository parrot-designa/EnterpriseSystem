package com.enterprisesystem.babymain.service;

import com.enterprisesystem.babymain.model.dto.SellerDto;
import com.enterprisesystem.babymain.model.dto.SellerPageRequest;
import com.enterprisesystem.babymain.model.response.PageResult;

public interface SellerService {
    SellerDto addSeller(SellerDto sellerDto);

    /**
     * 分页查询商家
     */
    PageResult<SellerDto> querySeller(SellerPageRequest request);
}
