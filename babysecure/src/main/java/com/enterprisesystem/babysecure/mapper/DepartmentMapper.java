package com.enterprisesystem.babysecure.mapper;

import com.enterprisesystem.babysecure.model.entity.DepartmentEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门 Mapper 接口
 *
 * 【重要概念】Mapper 是什么？
 * - Mapper 是一个接口（Interface），不是类
 * - MyBatis 会自动生成这个接口的实现类
 * - 我们只需要定义接口方法和 SQL 注解，不需要写实现代码
 *
 * @Mapper 注解：告诉 Spring Boot 这是一个 MyBatis Mapper
 * - Spring Boot 启动时会自动扫描并注册这个 Mapper
 * - 这样就可以在其他类中通过 @Resource 或 @Autowired 注入使用
 */
@Mapper
public interface DepartmentMapper {

    // ==================== 查询操作 ====================

    /**
     * 查询所有部门
     *
     * @Select：MyBatis 注解，直接写 SQL 语句
     * - 不需要写 XML 配置文件
     * - SQL 直接写在注解中，简单直观
     *
     * @return 所有部门列表
     */
    @Select("SELECT * FROM department")
    List<DepartmentEntity> selectAll();

    /**
     * 根据 ID 查询部门
     *
     * @Param("id")：给方法参数起别名，SQL 中使用 #{id} 引用
     * #{id}：MyBatis 参数占位符，会自动替换为方法参数值
     * 例如：方法调用 selectById(5)，SQL 变成 SELECT * FROM department WHERE id = 5
     *
     * @param id 部门ID
     * @return 部门实体对象
     */
    @Select("SELECT * FROM department WHERE id = #{id}")
    DepartmentEntity selectById(@Param("id") Integer id);

    /**
     * 根据部门编码查询
     *
     * @param code 部门编码（如 DEPT001）
     * @return 部门实体对象
     */
    @Select("SELECT * FROM department WHERE code = #{code}")
    DepartmentEntity selectByCode(@Param("code") String code);

    /**
     * 根据父部门ID查询所有子部门
     *
     * 应用场景：构建树形结构时，查询某个部门下的所有子部门
     *
     * @param parentId 父部门ID，null 表示查询顶级部门
     * @return 子部门列表
     */
    @Select("SELECT * FROM department WHERE parent_id = #{parentId} ORDER BY sort_order ASC")
    List<DepartmentEntity> selectByParentId(@Param("parentId") Integer parentId);

    // ==================== 插入操作 ====================

    /**
     * 插入部门
     *
     * @Insert：执行 INSERT 语句
     * #{code}、#{name}：会自动从 DepartmentEntity 对象中获取对应字段的值
     *
     * @Options(useGeneratedKeys = true, keyProperty = "id")：
     * - useGeneratedKeys = true：使用数据库自动生成的主键
     * - keyProperty = "id"：将生成的主键值设置到对象的 id 字段
     *
     * 执行后，entity.getId() 就能获取到数据库自动生成的 ID
     *
     * @param entity 部门实体对象
     * @return 影响的行数（1 表示成功，0 表示失败）
     */
    @Insert("INSERT INTO department(code, name, parent_id, status, sort_order) " +
            "VALUES(#{code}, #{name}, #{parentId}, #{status}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DepartmentEntity entity);

    // ==================== 更新操作 ====================

    /**
     * 更新部门信息
     *
     * @Update：执行 UPDATE 语句
     * 只更新指定的字段（code、name、parentId 等）
     * WHERE id = #{id}：指定更新哪条记录
     *
     * @param entity 部门实体对象（必须包含 id）
     * @return 影响的行数
     */
    @Update("UPDATE department SET " +
            "code = #{code}, " +
            "name = #{name}, " +
            "parent_id = #{parentId}, " +
            "status = #{status}, " +
            "sort_order = #{sortOrder} " +
            "WHERE id = #{id}")
    int update(DepartmentEntity entity);

    /**
     * 选择性更新（只更新非空字段）
     *
     * 使用 <if> 标签实现条件判断：
     * <if test='code != null'>：如果 code 字段不为 null，才更新这个字段
     *
     * 这样可以实现：只想更新部门名称，只需要设置 name 字段，其他字段为 null
     *
     * @param entity 部门实体对象
     * @return 影响的行数
     */
    @Update("<script>" +
            "UPDATE department " +
            "<set>" +
            "<if test='code != null'>code = #{code},</if>" +
            "<if test='name != null'>name = #{name},</if>" +
            "<if test='parentId != null'>parent_id = #{parentId},</if>" +
            "<if test='status != null'>status = #{status},</if>" +
            "<if test='sortOrder != null'>sort_order = #{sortOrder},</if>" +
            "</set>" +
            "WHERE id = #{id}" +
            "</script>")
    int updateSelective(DepartmentEntity entity);

    // ==================== 删除操作 ====================

    /**
     * 根据 ID 删除部门
     *
     * ⚠️ 注意：实际开发中，删除部门前需要检查：
     * 1. 该部门下是否有子部门
     * 2. 该部门下是否有员工
     * 3. 是否有关联数据
     *
     * @param id 部门ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM department WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);

    /**
     * 批量删除部门
     *
     * 使用 <foreach> 标签遍历列表：
     * collection='ids'：参数名称（方法参数的 @Param("ids")）
     * item='id'：遍历时的每个元素名称
     * separator=','：每个元素之间的分隔符
     *
     * 例如：ids = [1, 2, 3]
     * SQL 变成：DELETE FROM department WHERE id IN (1, 2, 3)
     *
     * @param ids 部门ID列表
     * @return 影响的行数
     */
    @Delete("<script>" +
            "DELETE FROM department WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int deleteByIds(@Param("ids") List<Integer> ids);

    // ==================== 统计查询 ====================

    /**
     * 统计部门总数
     *
     * COUNT(*)：统计记录数
     *
     * @return 部门总数
     */
    @Select("SELECT COUNT(*) FROM department")
    Long count();

    /**
     * 根据父部门ID统计子部门数量
     *
     * @param parentId 父部门ID
     * @return 子部门数量
     */
    @Select("SELECT COUNT(*) FROM department WHERE parent_id = #{parentId}")
    Long countByParentId(@Param("parentId") Integer parentId);
}
