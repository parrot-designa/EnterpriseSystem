package com.enterprisesystem.babycommon.query;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * MyBatis Lambda 查询构造器
 * 支持类型安全的动态 SQL 构建，避免硬编码字段名
 *
 * @param <T> 实体类类型
 * @author Claude Code
 * @date 2026-01-04
 */
public class LambdaQueryWrapper<T> {

    private final StringBuilder whereSql = new StringBuilder();
    private final List<Object> params = new ArrayList<>();
    private final List<String> orderByConditions = new ArrayList<>();
    private boolean hasCondition = false;

    /**
     * 等于 (=)
     *
     * @param column Lambda 表达式引用字段
     * @param value  值
     * @return this
     */
    public LambdaQueryWrapper<T> eq(SerializableFunction<T, ?> column, Object value) {
        return addCondition(column, " = ", value, value != null);
    }

    /**
     * 不等于 (!=)
     *
     * @param column Lambda 表达式引用字段
     * @param value  值
     * @return this
     */
    public LambdaQueryWrapper<T> ne(SerializableFunction<T, ?> column, Object value) {
        return addCondition(column, " != ", value, value != null);
    }

    /**
     * 大于 (>)
     *
     * @param column Lambda 表达式引用字段
     * @param value  值
     * @return this
     */
    public LambdaQueryWrapper<T> gt(SerializableFunction<T, ?> column, Object value) {
        return addCondition(column, " > ", value, value != null);
    }

    /**
     * 大于等于 (>=)
     *
     * @param column Lambda 表达式引用字段
     * @param value  值
     * @return this
     */
    public LambdaQueryWrapper<T> ge(SerializableFunction<T, ?> column, Object value) {
        return addCondition(column, " >= ", value, value != null);
    }

    /**
     * 小于 (<)
     *
     * @param column Lambda 表达式引用字段
     * @param value  值
     * @return this
     */
    public LambdaQueryWrapper<T> lt(SerializableFunction<T, ?> column, Object value) {
        return addCondition(column, " < ", value, value != null);
    }

    /**
     * 小于等于 (<=)
     *
     * @param column Lambda 表达式引用字段
     * @param value  值
     * @return this
     */
    public LambdaQueryWrapper<T> le(SerializableFunction<T, ?> column, Object value) {
        return addCondition(column, " <= ", value, value != null);
    }

    /**
     * 模糊查询 (LIKE %value%)
     *
     * @param column Lambda 表达式引用字段
     * @param value  值
     * @return this
     */
    public LambdaQueryWrapper<T> like(SerializableFunction<T, ?> column, String value) {
        if (StringUtils.isNotBlank(value)) {
            return addCondition(column, " LIKE ", "%" + value + "%", true);
        }
        return this;
    }

    /**
     * 左模糊查询 (LIKE %value)
     *
     * @param column Lambda 表达式引用字段
     * @param value  值
     * @return this
     */
    public LambdaQueryWrapper<T> likeLeft(SerializableFunction<T, ?> column, String value) {
        if (StringUtils.isNotBlank(value)) {
            return addCondition(column, " LIKE ", "%" + value, true);
        }
        return this;
    }

    /**
     * 右模糊查询 (LIKE value%)
     *
     * @param column Lambda 表达式引用字段
     * @param value  值
     * @return this
     */
    public LambdaQueryWrapper<T> likeRight(SerializableFunction<T, ?> column, String value) {
        if (StringUtils.isNotBlank(value)) {
            return addCondition(column, " LIKE ", value + "%", true);
        }
        return this;
    }

    /**
     * IN 查询
     *
     * @param column Lambda 表达式引用字段
     * @param values 值集合
     * @return this
     */
    public LambdaQueryWrapper<T> in(SerializableFunction<T, ?> column, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            String columnName = LambdaUtils.getFieldName(column, true);
            addWherePrefix();
            whereSql.append(columnName).append(" IN (");
            String placeholders = String.join(",", Collections.nCopies(values.size(), "?"));
            whereSql.append(placeholders).append(")");
            params.addAll(values);
            hasCondition = true;
        }
        return this;
    }

    /**
     * NOT IN 查询
     *
     * @param column Lambda 表达式引用字段
     * @param values 值集合
     * @return this
     */
    public LambdaQueryWrapper<T> notIn(SerializableFunction<T, ?> column, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            String columnName = LambdaUtils.getFieldName(column, true);
            addWherePrefix();
            whereSql.append(columnName).append(" NOT IN (");
            String placeholders = String.join(",", Collections.nCopies(values.size(), "?"));
            whereSql.append(placeholders).append(")");
            params.addAll(values);
            hasCondition = true;
        }
        return this;
    }

    /**
     * BETWEEN 查询
     *
     * @param column Lambda 表达式引用字段
     * @param start  起始值
     * @param end    结束值
     * @return this
     */
    public LambdaQueryWrapper<T> between(SerializableFunction<T, ?> column, Object start, Object end) {
        if (start != null && end != null) {
            String columnName = LambdaUtils.getFieldName(column, true);
            addWherePrefix();
            whereSql.append(columnName).append(" BETWEEN ? AND ?");
            params.add(start);
            params.add(end);
            hasCondition = true;
        }
        return this;
    }

    /**
     * IS NULL 查询
     *
     * @param column Lambda 表达式引用字段
     * @return this
     */
    public LambdaQueryWrapper<T> isNull(SerializableFunction<T, ?> column) {
        String columnName = LambdaUtils.getFieldName(column, true);
        addWherePrefix();
        whereSql.append(columnName).append(" IS NULL");
        hasCondition = true;
        return this;
    }

    /**
     * IS NOT NULL 查询
     *
     * @param column Lambda 表达式引用字段
     * @return this
     */
    public LambdaQueryWrapper<T> isNotNull(SerializableFunction<T, ?> column) {
        String columnName = LambdaUtils.getFieldName(column, true);
        addWherePrefix();
        whereSql.append(columnName).append(" IS NOT NULL");
        hasCondition = true;
        return this;
    }

    /**
     * 升序排序 (ORDER BY column ASC)
     *
     * @param column Lambda 表达式引用字段
     * @return this
     */
    public LambdaQueryWrapper<T> orderByAsc(SerializableFunction<T, ?> column) {
        String columnName = LambdaUtils.getFieldName(column, true);
        orderByConditions.add(columnName + " ASC");
        return this;
    }

    /**
     * 降序排序 (ORDER BY column DESC)
     *
     * @param column Lambda 表达式引用字段
     * @return this
     */
    public LambdaQueryWrapper<T> orderByDesc(SerializableFunction<T, ?> column) {
        String columnName = LambdaUtils.getFieldName(column, true);
        orderByConditions.add(columnName + " DESC");
        return this;
    }

    /**
     * 添加自定义 WHERE 条件（手动拼接）
     *
     * @param conditionSql WHERE 条件 SQL
     * @param params        参数
     * @return this
     */
    public LambdaQueryWrapper<T> apply(String conditionSql, Object... params) {
        if (StringUtils.isNotBlank(conditionSql)) {
            addWherePrefix();
            whereSql.append(conditionSql);
            if (params != null) {
                Collections.addAll(this.params, params);
            }
            hasCondition = true;
        }
        return this;
    }

    /**
     * 获取 WHERE 子句 SQL（包含 WHERE 关键字）
     *
     * @return WHERE SQL
     */
    public String getWhereSql() {
        StringBuilder sql = new StringBuilder();
        if (whereSql.length() > 0) {
            sql.append(" WHERE ").append(whereSql);
        }
        return sql.toString();
    }

    /**
     * 获取完整 SQL（包含 WHERE 和 ORDER BY）
     *
     * @return 完整 SQL
     */
    public String getSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(getWhereSql());
        if (!orderByConditions.isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", orderByConditions));
        }
        return sql.toString();
    }

    /**
     * 获取参数列表
     *
     * @return 参数列表
     */
    public List<Object> getParams() {
        return new ArrayList<>(params);
    }

    /**
     * 清空所有条件
     */
    public void clear() {
        whereSql.setLength(0);
        params.clear();
        orderByConditions.clear();
        hasCondition = false;
    }

    /**
     * 是否有条件
     *
     * @return true 如果有条件
     */
    public boolean hasCondition() {
        return hasCondition || !orderByConditions.isEmpty();
    }

    /**
     * 添加 WHERE 前缀
     */
    private void addWherePrefix() {
        if (hasCondition) {
            whereSql.append(" AND ");
        }
    }

    /**
     * 添加条件
     *
     * @param column     Lambda 表达式
     * @param operator   操作符
     * @param value      值
     * @param condition  是否添加条件
     * @return this
     */
    private LambdaQueryWrapper<T> addCondition(SerializableFunction<T, ?> column, String operator, Object value, boolean condition) {
        if (!condition) {
            return this;
        }
        String columnName = LambdaUtils.getFieldName(column, true);
        addWherePrefix();
        whereSql.append(columnName).append(operator).append("?");
        params.add(value);
        hasCondition = true;
        return this;
    }

    @Override
    public String toString() {
        return "LambdaQueryWrapper{" +
                "sql='" + getSql() + '\'' +
                ", params=" + params +
                '}';
    }
}
