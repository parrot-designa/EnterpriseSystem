package com.enterprisesystem.babycommon.query;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的函数式接口，用于 Lambda 表达式的字段引用
 *
 * @param <T> 实体类类型
 * @param <R> 字段类型
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
}
