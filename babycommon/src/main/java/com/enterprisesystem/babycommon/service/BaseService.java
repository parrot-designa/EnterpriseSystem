package com.enterprisesystem.babycommon.service;

import com.enterprisesystem.babycommon.model.PageRequest;
import com.enterprisesystem.babycommon.model.PageResult;

import java.util.List;

/**
 * 通用 Service 接口
 * 提供基础的 CRUD 业务操作方法定义
 *
 * @param <T> 实体类型
 * @param <ID> 主键类型
 * @author Claude Code
 */
public interface BaseService<T, ID> {

    /**
     * 保存记录
     *
     * @param entity 实体对象
     * @return 保存后的实体对象
     */
    T save(T entity);

    /**
     * 批量保存记录
     *
     * @param entities 实体对象列表
     * @return 保存的记录数
     */
    int batchSave(List<T> entities);

    /**
     * 更新记录
     *
     * @param entity 实体对象
     * @return 更新的记录数
     */
    int update(T entity);

    /**
     * 保存或更新记录（根据主键是否存在判断）
     *
     * @param entity 实体对象
     * @return 保存或更新后的实体对象
     */
    T saveOrUpdate(T entity);

    /**
     * 根据主键删除记录
     *
     * @param id 主键ID
     * @return 删除的记录数
     */
    int deleteById(ID id);

    /**
     * 根据主键批量删除记录
     *
     * @param ids 主键ID列表
     * @return 删除的记录数
     */
    int deleteByIds(List<ID> ids);

    /**
     * 根据主键查询记录
     *
     * @param id 主键ID
     * @return 实体对象
     */
    T getById(ID id);

    /**
     * 根据主键批量查询记录
     *
     * @param ids 主键ID列表
     * @return 实体对象列表
     */
    List<T> getByIds(List<ID> ids);

    /**
     * 查询所有记录
     *
     * @return 实体对象列表
     */
    List<T> list();

    /**
     * 根据条件查询记录列表
     *
     * @param entity 查询条件实体
     * @return 实体对象列表
     */
    List<T> listByCondition(T entity);

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
     * 分页查询记录
     *
     * @param pageRequest 分页请求对象
     * @return 分页结果对象
     */
    PageResult<T> page(PageRequest pageRequest);

    /**
     * 根据条件分页查询记录
     *
     * @param entity      查询条件实体
     * @param pageRequest 分页请求对象
     * @return 分页结果对象
     */
    PageResult<T> pageByCondition(T entity, PageRequest pageRequest);

    /**
     * 查询记录是否存在
     *
     * @param id 主键ID
     * @return true-存在，false-不存在
     */
    boolean existsById(ID id);

}
