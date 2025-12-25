package com.enterprisesystem.babymain.mapper;

import com.enterprisesystem.babymain.dto.SellerDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SellerMapper {

    /**
     * 查询所有商家
     */
    @Select("SELECT * FROM seller")
    List<SellerDto> selectAll();

    /**
     * 根据ID查询商家
     */
    @Select("SELECT * FROM seller WHERE id = #{id}")
    SellerDto selectById(@Param("id") Integer id);

    /**
     * 根据商家代码查询
     */
    @Select("SELECT * FROM seller WHERE code = #{code}")
    SellerDto selectByCode(@Param("code") String code);

    /**
     * 插入商家
     */
    @Insert("INSERT INTO seller(code, name) VALUES(#{code}, #{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SellerDto sellerDto);

    /**
     * 更新商家
     */
    @Update("UPDATE seller SET code = #{code}, name = #{name} WHERE id = #{id}")
    int update(SellerDto sellerDto);

    /**
     * 根据ID删除商家
     */
    @Delete("DELETE FROM seller WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);

    /**
     * 批量插入商家
     */
    @Insert("<script>" +
            "INSERT INTO seller(code, name) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.code}, #{item.name})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("list") List<SellerDto> sellerDtos);
}
