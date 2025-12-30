package com.enterprisesystem.babycommon.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通用 Mapper 接口
 * 提供基础的 CRUD 操作方法定义，减少重复代码
 *
 * @param <T> 实体类型
 * @param <ID> 主键类型
 * @author Claude Code
 */
public interface BaseMapper<T, ID> {

    /**
     * 插入记录
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int insert(T entity);

    /**
     * 批量插入记录
     *
     * @param entities 实体对象列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<T> entities);

    /**
     * 选择性插入记录（忽略 null 字段）
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int insertSelective(T entity);

    /**
     * 更新记录
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int update(T entity);

    /**
     * 选择性更新记录（忽略 null 字段）
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int updateSelective(T entity);

    /**
     * 批量更新记录
     *
     * @param entities 实体对象列表
     * @return 影响行数
     */
    int batchUpdate(@Param("list") List<T> entities);

    /**
     * 根据主键删除记录
     *
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") ID id);

    /**
     * 根据主键批量删除记录
     *
     * @param ids 主键ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<ID> ids);

    /**
     * 根据条件删除记录
     *
     * @param entity 查询条件实体
     * @return 影响行数
     */
    int deleteByCondition(T entity);

    /**
     * 根据主键查询记录
     *
     * @param id 主键ID
     * @return 实体对象
     */
    T selectById(@Param("id") ID id);

    /**
     * 根据主键批量查询记录
     *
     * @param ids 主键ID列表
     * @return 实体对象列表
     */
    List<T> selectByIds(@Param("ids") List<ID> ids);

    /**
     * 查询所有记录
     *
     * @return 实体对象列表
     */
    List<T> selectAll();

    /**
     * 根据条件查询单条记录
     *
     * @param entity 查询条件实体
     * @return 实体对象
     */
    T selectOne(T entity);

    /**
     * 根据条件查询记录列表
     *
     * @param entity 查询条件实体
     * @return 实体对象列表
     */
    List<T> selectByCondition(T entity);

    /**
     * 统计记录总数
     *
     * @return 记录总数
     */
    Long count();

    /**
     * 根据条件统计记录数
     *
     * @param entity 查询条件实体
     * @return 记录总数
     */
    Long countByCondition(T entity);

    /**
     * 查询记录是否存在
     *
     * @param id 主键ID
     * @return true-存在，false-不存在
     */
    default boolean existsById(ID id) {
        return selectById(id) != null;
    }

}
