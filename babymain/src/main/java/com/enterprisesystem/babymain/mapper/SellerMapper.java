package com.enterprisesystem.babymain.mapper;

import com.enterprisesystem.babymain.model.dto.SellerDto;
import com.enterprisesystem.babymain.model.dto.SellerPageRequest;
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

    /**
     * 分页查询商家
     */
    @Select("<script>" +
            "SELECT * FROM seller " +
            "<where>" +
            "<if test='code != null and code != \"\"'>" +
            "AND code LIKE CONCAT('%', #{code}, '%') " +
            "</if>" +
            "<if test='name != null and name != \"\"'>" +
            "AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "</where>" +
            "ORDER BY id DESC " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<SellerDto> selectByPage(SellerPageRequest request);

    /**
     * 统计商家总数（用于分页）
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM seller " +
            "<where>" +
            "<if test='code != null and code != \"\"'>" +
            "AND code LIKE CONCAT('%', #{code}, '%') " +
            "</if>" +
            "<if test='name != null and name != \"\"'>" +
            "AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "</where>" +
            "</script>")
    Long countByPage(SellerPageRequest request);
}
