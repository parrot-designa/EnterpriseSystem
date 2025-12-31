package com.enterprisesystem.babycommon.service.impl;

import com.enterprisesystem.babycommon.mapper.BaseMapper;
import com.enterprisesystem.babycommon.model.PageRequest;
import com.enterprisesystem.babycommon.model.PageResult;
import com.enterprisesystem.babycommon.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * 通用 Service 实现类
 * 提供基础的 CRUD 业务操作实现
 *
 * @param <M> Mapper 类型
 * @param <T> 实体类型
 * @param <ID> 主键类型
 * @author Claude Code
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T, ID>, T, ID> implements BaseService<T, ID> {

    @Autowired
    protected M mapper;

    /**
     * 获取当前使用的 Mapper
     *
     * @return Mapper 对象
     */
    protected M getMapper() {
        return mapper;
    }

    @Override
    public T save(T entity) {
        mapper.insert(entity);
        return entity;
    }

    @Override
    public int batchSave(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        return mapper.batchInsert(entities);
    }

    @Override
    public int update(T entity) {
        return mapper.update(entity);
    }

    @Override
    public T saveOrUpdate(T entity) {
        // 子类需要根据实际情况实现此方法
        // 因为需要知道如何判断记录是否存在
        throw new UnsupportedOperationException(
                "saveOrUpdate 方法需要子类根据具体业务实现，建议通过主键判断记录是否存在");
    }

    @Override
    public int deleteById(ID id) {
        return mapper.deleteById(id);
    }

    @Override
    public int deleteByIds(List<ID> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return mapper.deleteByIds(ids);
    }

    @Override
    public T getById(ID id) {
        return mapper.selectById(id);
    }

    @Override
    public List<T> getByIds(List<ID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return mapper.selectByIds(ids);
    }

    @Override
    public List<T> list() {
        return mapper.selectAll();
    }

    @Override
    public List<T> listByCondition(T entity) {
        return mapper.selectByCondition(entity);
    }

    @Override
    public Long count() {
        return mapper.count();
    }

    @Override
    public Long countByCondition(T entity) {
        return mapper.countByCondition(entity);
    }

    @Override
    public PageResult<T> page(PageRequest pageRequest) {
        if (pageRequest == null) {
            pageRequest = new PageRequest();
        }
        pageRequest.validate();

        // 子类需要实现具体的分页查询逻辑
        throw new UnsupportedOperationException(
                "page 方法需要子类根据具体业务实现，因为不同实体的分页查询条件不同");
    }

    @Override
    public PageResult<T> pageByCondition(T entity, PageRequest pageRequest) {
        if (pageRequest == null) {
            pageRequest = new PageRequest();
        }
        pageRequest.validate();

        // 子类需要实现具体的分页查询逻辑
        throw new UnsupportedOperationException(
                "pageByCondition 方法需要子类根据具体业务实现，因为不同实体的分页查询条件不同");
    }

    @Override
    public boolean existsById(ID id) {
        return mapper.existsById(id);
    }

}
