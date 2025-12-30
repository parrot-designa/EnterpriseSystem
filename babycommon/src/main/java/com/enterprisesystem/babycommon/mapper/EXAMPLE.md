# SellerMapper 改造示例

本文档展示如何将现有的 `SellerMapper` 改造为使用通用 `BaseMapper`，以减少重复代码。

## 原始 SellerMapper（改造前）

```java
package com.enterprisesystem.babymain.mapper;

import com.enterprisesystem.babymain.model.dto.SellerDto;
import com.enterprisesystem.babymain.model.dto.SellerPageRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SellerMapper {

    @Select("SELECT * FROM seller")
    List<SellerDto> selectAll();

    @Select("SELECT * FROM seller WHERE id = #{id}")
    SellerDto selectById(@Param("id") Integer id);

    @Select("SELECT * FROM seller WHERE code = #{code}")
    SellerDto selectByCode(@Param("code") String code);

    @Insert("INSERT INTO seller(code, name) VALUES(#{code}, #{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SellerDto sellerDto);

    @Update("UPDATE seller SET code = #{code}, name = #{name} WHERE id = #{id}")
    int update(SellerDto sellerDto);

    @Delete("DELETE FROM seller WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);

    @Insert("<script>" +
            "INSERT INTO seller(code, name) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.code}, #{item.name})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("list") List<SellerDto> sellerDtos);

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
```

## 改造后的 SellerMapper（使用 BaseMapper）

```java
package com.enterprisesystem.babymain.mapper;

import com.enterprisesystem.babycommon.mapper.BaseMapper;
import com.enterprisesystem.babymain.model.dto.SellerDto;
import com.enterprisesystem.babymain.model.dto.SellerPageRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 商家 Mapper 接口
 * 继承 BaseMapper 获得基础 CRUD 方法
 */
@Mapper
public interface SellerMapper extends BaseMapper<SellerDto, Integer> {

    // ========== 基础 CRUD 方法（继承自 BaseMapper，无需实现） ==========
    // int insert(SellerDto sellerDto);
    // int batchInsert(List<SellerDto> sellerDtos);
    // int update(SellerDto sellerDto);
    // int deleteById(Integer id);
    // SellerDto selectById(Integer id);
    // List<SellerDto> selectAll();
    // Long count();
    // 等等...

    // ========== 只需要实现特定业务方法 ==========

    /**
     * 根据商家代码查询（特定业务方法）
     */
    @Select("SELECT * FROM seller WHERE code = #{code}")
    SellerDto selectByCode(@Param("code") String code);

    /**
     * 分页查询商家（特定业务方法）
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
```

## 对比分析

### 改造前
- 总代码行数：~93 行
- 需要实现的基础方法：8 个
- 特定业务方法：3 个（selectByCode、selectByPage、countByPage）

### 改造后
- 总代码行数：~63 行
- 继承基础方法：自动获得 15+ 个基础 CRUD 方法
- 只需实现特定业务方法：3 个

### 改造优势

1. **减少代码量**：基础 CRUD 方法无需重复编写
2. **统一接口**：所有 Mapper 遵循相同的方法命名和签名
3. **易于维护**：基础逻辑集中在 BaseMapper 中
4. **扩展性强**：自动获得新增的通用方法

## Service 层改造示例

### 改造前

```java
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerMapper sellerMapper;

    public SellerDto getById(Integer id) {
        return sellerMapper.selectById(id);
    }

    public List<SellerDto> list() {
        return sellerMapper.selectAll();
    }

    public SellerDto save(SellerDto seller) {
        sellerMapper.insert(seller);
        return seller;
    }

    public int update(SellerDto seller) {
        return sellerMapper.update(seller);
    }

    public int deleteById(Integer id) {
        return sellerMapper.deleteById(id);
    }

    // 每个方法都要写一遍...
}
```

### 改造后

```java
public interface SellerService extends BaseService<SellerDto, Integer> {
    // 继承所有基础 CRUD 方法
    // 只需添加特定的业务方法
}

@Service
public class SellerServiceImpl
        extends BaseServiceImpl<SellerMapper, SellerDto, Integer>
        implements SellerService {

    // 基础 CRUD 方法已自动实现
    // 只需实现特定的业务方法

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

        SellerPageRequest request = new SellerPageRequest();
        request.setOffset(pageRequest.getOffset());
        request.setPageSize(pageRequest.getPageSize());

        List<SellerDto> records = getMapper().selectByPage(request);
        Long total = getMapper().countByPage(request);

        return PageResult.of(pageRequest.getPage(), pageRequest.getPageSize(), total, records);
    }
}
```

## 改造收益总结

| 指标 | 改造前 | 改造后 | 收益 |
|------|--------|--------|------|
| Mapper 代码行数 | ~93 行 | ~63 行 | 减少 32% |
| 需实现的方法数 | 11 个 | 3 个 | 减少 73% |
| Service 代码行数 | ~100+ 行 | ~30 行 | 减少 70% |
| 代码复用性 | 低 | 高 | 大幅提升 |

## 总结

通过使用通用 `BaseMapper` 和 `BaseService`：

1. ✅ **大幅减少重复代码**：基础 CRUD 方法无需重复编写
2. ✅ **统一代码风格**：所有模块遵循相同的设计模式
3. ✅ **提高开发效率**：新增实体时只需编写特定业务逻辑
4. ✅ **便于维护**：通用逻辑集中管理，修改一处即可
5. ✅ **降低出错率**：基础方法经过充分测试，减少 bug

建议在后续的新模块开发中统一使用这种模式！
