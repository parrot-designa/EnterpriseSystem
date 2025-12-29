package com.enterprisesystem.babymain.service.impl;

import com.enterprisesystem.babymain.model.dto.SellerDto;
import com.enterprisesystem.babymain.model.dto.SellerPageRequest;
import com.enterprisesystem.babymain.model.response.PageResult;
import com.enterprisesystem.babymain.mapper.SellerMapper;
import com.enterprisesystem.babymain.service.SellerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private SellerMapper sellerMapper;

    @Override
    public SellerDto addSeller(SellerDto sellerDto) {
        sellerMapper.insert(sellerDto);
        return sellerDto;
    }

    @Override
    public PageResult<SellerDto> querySeller(SellerPageRequest request) {
        Long total = sellerMapper.countByPage(request);
        if (total == 0) {
            return PageResult.empty(request.getPageNum(), request.getPageSize());
        }
        List<SellerDto> list = sellerMapper.selectByPage(request);
        return new PageResult<>(request.getPageNum(), request.getPageSize(), total, list);
    }
}
