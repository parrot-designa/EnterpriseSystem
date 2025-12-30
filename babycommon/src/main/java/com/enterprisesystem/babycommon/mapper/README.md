# 通用 Mapper 和 Service 使用指南

本模块提供了通用的 Mapper 和 Service 基类，用于减少重复代码，提高开发效率。

## 组件说明

### 1. BaseMapper<T, ID>

通用 Mapper 接口，定义了基础的 CRUD 方法：

- **插入**：`insert()`, `batchInsert()`, `insertSelective()`
- **更新**：`update()`, `updateSelective()`, `batchUpdate()`
- **删除**：`deleteById()`, `deleteByIds()`, `deleteByCondition()`
- **查询**：`selectById()`, `selectByIds()`, `selectAll()`, `selectOne()`, `selectByCondition()`
- **统计**：`count()`, `countByCondition()`
- **判断**：`existsById()`

### 2. BaseService<T, ID> 和 BaseServiceImpl<M, T, ID>

通用 Service 接口和实现类，封装业务逻辑：

- **保存**：`save()`, `batchSave()`, `saveOrUpdate()`
- **更新**：`update()`
- **删除**：`deleteById()`, `deleteByIds()`
- **查询**：`getById()`, `getByIds()`, `list()`, `listByCondition()`
- **统计**：`count()`, `countByCondition()`
- **分页**：`page()`, `pageByCondition()`
- **判断**：`existsById()`

### 3. PageRequest 和 PageResult<T>

分页查询支持：

- **PageRequest**：分页请求对象，包含页码、每页大小、排序字段等
- **PageResult<T>**：分页结果对象，包含数据列表、总数、总页数等信息

## 使用示例

### 示例 1：定义 Mapper 接口

```java
package com.enterprisesystem.babymain.mapper;

import com.enterprisesystem.babycommon.mapper.BaseMapper;
import com.enterprisesystem.babymain.model.dto.SellerDto;
import org.apache.ibatis.annotations.*;

/**
 * 商家 Mapper 接口
 */
@Mapper
public interface SellerMapper extends BaseMapper<SellerDto, Integer> {

    /**
     * 插入商家
     */
    @Override
    @Insert("INSERT INTO seller(code, name) VALUES(#{code}, #{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SellerDto sellerDto);

    /**
     * 根据ID查询商家
     */
    @Override
    @Select("SELECT * FROM seller WHERE id = #{id}")
    SellerDto selectById(@Param("id") Integer id);

    /**
     * 查询所有商家
     */
    @Override
    @Select("SELECT * FROM seller")
    List<SellerDto> selectAll();

    /**
     * 更新商家
     */
    @Override
    @Update("UPDATE seller SET code = #{code}, name = #{name} WHERE id = #{id}")
    int update(SellerDto sellerDto);

    /**
     * 根据ID删除商家
     */
    @Override
    @Delete("DELETE FROM seller WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);

    // 其他自定义方法...
}
```

### 示例 2：定义 Service 接口

```java
package com.enterprisesystem.babymain.service;

import com.enterprisesystem.babycommon.service.BaseService;
import com.enterprisesystem.babymain.model.dto.SellerDto;

/**
 * 商家 Service 接口
 */
public interface SellerService extends BaseService<SellerDto, Integer> {

    // 继承 BaseService 的所有方法
    // 可以添加额外的业务方法
}
```

### 示例 3：实现 Service 类

```java
package com.enterprisesystem.babymain.impl;

import com.enterprisesystem.babycommon.service.impl.BaseServiceImpl;
import com.enterprisesystem.babycommon.model.PageRequest;
import com.enterprisesystem.babycommon.model.PageResult;
import com.enterprisesystem.babymain.mapper.SellerMapper;
import com.enterprisesystem.babymain.model.dto.SellerDto;
import com.enterprisesystem.babymain.service.SellerService;
import org.springframework.stereotype.Service;

/**
 * 商家 Service 实现类
 */
@Service
public class SellerServiceImpl extends BaseServiceImpl<SellerMapper, SellerDto, Integer>
        implements SellerService {

    @Override
    public SellerDto saveOrUpdate(SellerDto entity) {
        if (entity.getId() != null && existsById(entity.getId())) {
            update(entity);
            return entity;
        } else {
            return save(entity);
        }
    }

    @Override
    public PageResult<SellerDto> page(PageRequest pageRequest) {
        pageRequest.validate();
        List<SellerDto> records = getMapper().selectByPage(/* 构建查询条件 */);
        Long total = getMapper().countByPage(/* 构建查询条件 */);
        return PageResult.of(pageRequest.getPage(), pageRequest.getPageSize(), total, records);
    }

    @Override
    public PageResult<SellerDto> pageByCondition(SellerDto entity, PageRequest pageRequest) {
        pageRequest.validate();
        List<SellerDto> records = getMapper().selectByConditionAndPage(entity, pageRequest);
        Long total = getMapper().countByCondition(entity);
        return PageResult.of(pageRequest.getPage(), pageRequest.getPageSize(), total, records);
    }
}
```

### 示例 4：在 Controller 中使用

```java
package com.enterprisesystem.babymain.controller;

import com.enterprisesystem.babycommon.model.PageRequest;
import com.enterprisesystem.babycommon.model.PageResult;
import com.enterprisesystem.babymain.model.dto.SellerDto;
import com.enterprisesystem.babymain.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商家 Controller
 */
@RestController
@RequestMapping("/api/v3/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    /**
     * 根据ID查询商家
     */
    @GetMapping("/{id}")
    public APIResult<SellerDto> getById(@PathVariable Integer id) {
        SellerDto seller = sellerService.getById(id);
        return APIResult.success(seller);
    }

    /**
     * 查询所有商家
     */
    @GetMapping("/list")
    public APIResult<List<SellerDto>> list() {
        List<SellerDto> sellers = sellerService.list();
        return APIResult.success(sellers);
    }

    /**
     * 分页查询商家
     */
    @GetMapping("/page")
    public APIResult<PageResult<SellerDto>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        PageResult<SellerDto> result = sellerService.page(pageRequest);
        return APIResult.success(result);
    }

    /**
     * 保存商家
     */
    @PostMapping("/save")
    public APIResult<SellerDto> save(@RequestBody SellerDto seller) {
        SellerDto saved = sellerService.save(seller);
        return APIResult.success(saved);
    }

    /**
     * 更新商家
     */
    @PutMapping("/update")
    public APIResult<Integer> update(@RequestBody SellerDto seller) {
        int count = sellerService.update(seller);
        return APIResult.success(count);
    }

    /**
     * 删除商家
     */
    @DeleteMapping("/{id}")
    public APIResult<Integer> delete(@PathVariable Integer id) {
        int count = sellerService.deleteById(id);
        return APIResult.success(count);
    }
}
```

## 最佳实践

1. **Mapper 层**：
   - 继承 `BaseMapper<T, ID>` 获得基础 CRUD 方法
   - 使用 MyBatis 注解实现基础方法
   - 只需要实现特定的业务查询方法

2. **Service 层**：
   - 继承 `BaseService<T, ID>` 定义接口
   - 继承 `BaseServiceImpl<M, T, ID>` 实现类
   - 重写需要自定义逻辑的方法（如 `saveOrUpdate()`, `page()` 等）

3. **Controller 层**：
   - 直接注入使用 Service，无需编写重复的 CRUD 代码
   - 专注于业务逻辑和参数校验

4. **分页查询**：
   - 使用 `PageRequest` 封装分页参数
   - 使用 `PageResult<T>` 返回分页结果
   - 子类需要实现具体的分页查询逻辑

## 注意事项

1. 通用方法使用 `@Param` 注解时，注意参数名称的一致性
2. 分页查询方法需要在子类中实现，因为不同实体的查询条件不同
3. `saveOrUpdate()` 方法需要子类根据具体业务实现判断逻辑
4. 批量操作时注意事务管理
5. 使用 `insertSelective()` 和 `updateSelective()` 可以避免更新 null 字段
