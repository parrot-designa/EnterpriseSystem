package com.enterprisesystem.babycommon.query;

/**
 * MyBatis SQL Provider 基类
 * 用于支持 @SelectProvider、@UpdateProvider、@DeleteProvider 注解
 *
 * @author Claude Code
 * @date 2026-01-04
 */
public class BaseSqlProvider<T> {

    private final Class<T> entityClass;
    private final String tableName;

    /**
     * 构造函数
     *
     * @param entityClass 实体类类型
     * @param tableName   数据库表名
     */
    public BaseSqlProvider(Class<T> entityClass, String tableName) {
        this.entityClass = entityClass;
        this.tableName = tableName;
    }

    /**
     * 生成查询 SQL
     * 使用方法：@SelectProvider(type = UserSqlProvider.class, method = "findByWrapper")
     *
     * @param wrapper Lambda 查询构造器
     * @return SQL 语句
     */
    public String findByWrapper(LambdaQueryWrapper<T> wrapper) {
        return "SELECT * FROM " + tableName + wrapper.getSql();
    }

    /**
     * 生成计数 SQL
     *
     * @param wrapper Lambda 查询构造器
     * @return SQL 语句
     */
    public String countByWrapper(LambdaQueryWrapper<T> wrapper) {
        return "SELECT COUNT(*) FROM " + tableName + wrapper.getWhereSql();
    }

    /**
     * 生成删除 SQL
     *
     * @param wrapper Lambda 查询构造器
     * @return SQL 语句
     */
    public String deleteByWrapper(LambdaQueryWrapper<T> wrapper) {
        return "DELETE FROM " + tableName + wrapper.getWhereSql();
    }

    /**
     * 生成更新 SQL（需要额外拼接 SET 子句）
     *
     * @param wrapper Lambda 查询构造器
     * @param setSql  SET 子句（如："name = ?, age = ?"）
     * @return SQL 语句
     */
    public String updateByWrapper(LambdaQueryWrapper<T> wrapper, String setSql) {
        return "UPDATE " + tableName + " SET " + setSql + wrapper.getWhereSql();
    }
}
