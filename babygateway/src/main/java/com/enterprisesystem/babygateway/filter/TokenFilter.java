package com.enterprisesystem.babygateway.filter;

import com.enterprisesystem.babycommon.exception.SystemRuntimeException;
import com.enterprisesystem.babycommon.utils.CommonStringUtil;
import com.enterprisesystem.babygateway.config.AclProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Token 全局过滤器
 * <p>
 * 实现 GlobalFilter 和 Ordered 接口，对所有请求进行统一的 Token 验证和处理
 * <p>
 * 功能包括：
 * <ul>
 *   <li>从请求头或 Cookie 中提取 Token</li>
 *   <li>基于黑白名单的访问控制</li>
 *   <li>支持多种 Token 处理策略（普通、工作流等）</li>
 * </ul>
 *
 * @author EnterpriseSystem
 * @since 1.0.0
 */
public class TokenFilter implements GlobalFilter, Ordered {

    /**
     * 调试标识请求头名称
     */
    private static final String PRINT_FLAG = "print";

    /**
     * Token 请求头/Cookie 的键名
     */
    private static final String TOKEN_KEY = "token";

    /**
     * ACL 访问控制列表提供者集合
     * <p>
     * 通过 Spring 自动注入，支持多个 ACL 提供者实现
     */
    @Autowired(required = false)
    private List<AclProvider> aclProviderList = new ArrayList<>();

    /**
     * Token 处理器集合
     * <p>
     * 支持多种 Token 处理策略，根据类型选择对应的处理器
     */
    @Autowired
    private List<TokenHandler> handlers;

    /**
     * 获取过滤器执行顺序
     * <p>
     * 值越小优先级越高，此处设置为 0 表示最高优先级
     *
     * @return 过滤器顺序值
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 过滤器核心处理方法
     * <p>
     * 对每个请求进行 Token 验证和处理：
     * <ol>
     *   <li>判断是否需要执行过滤（基于 ACL 黑白名单）</li>
     *   <li>从请求头获取 Token，如果不存在则尝试从 Cookie 中获取</li>
     *   <li>根据 Token 处理器类型选择对应的处理器进行验证</li>
     * </ol>
     *
     * @param exchange 服务器Web交换机，包含请求和响应信息
     * @param chain 过滤器链，用于传递请求到下一个过滤器
     * @return 异步完成信号
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 判断是否需要执行过滤
        if (shouldFilter(exchange)) {
            ServerHttpRequest request = exchange.getRequest();
            String token = request.getHeaders().getFirst(TokenFilter.TOKEN_KEY);

            // 如果请求头中没有 Token，尝试从 Cookie 中提取
            if (CommonStringUtil.isBlank(token)) {
                pickCookieTokenInToHeader(exchange, chain);
            }

            String path = exchange.getRequest().getURI().getPath();
            String type = TokenHandlerType.NORMAL;

            // 根据类型获取对应的 Token 处理器
            TokenHandler handler = getHandlerByType(type);
            return handler.handle(exchange, chain);
        }
        // 不需要过滤，直接传递到下一个过滤器
        return chain.filter(exchange);
    }

    /**
     * 根据类型获取对应的 Token 处理器
     * <p>
     * 从已注册的处理器列表中查找匹配指定类型的处理器
     *
     * @param type 处理器类型（如：normal、workflow）
     * @return 匹配的 Token 处理器
     * @throws SystemRuntimeException 如果找不到对应类型的处理器
     */
    private TokenHandler getHandlerByType(String type) {
        TokenHandler handler = handlers.stream()
                .filter(t -> t.getHandlerBeanName().equals(type))
                .findFirst()
                .orElse(null);
        if (handler != null) {
            return handler;
        }
        throw new SystemRuntimeException("error handlerType:" + type);
    }

    /**
     * 刷新请求头中的 Token
     * <p>
     * 移除旧的 Token 并添加新的 Token
     *
     * @param httpHeaders HTTP 请求头
     * @param token 新的 Token 值
     */
    public static void refreshToken(HttpHeaders httpHeaders, String token) {
        httpHeaders.remove(TokenFilter.TOKEN_KEY);
        httpHeaders.add(TokenFilter.TOKEN_KEY, token);
    }

    /**
     * 从 Cookie 中提取 Token 并添加到请求头
     * <p>
     * 当请求头中不存在 Token 时，从 Cookie 中查找名为 "token" 的值，
     * 找到后将其添加到请求头中，便于后续处理
     *
     * @param exchange 服务器Web交换机
     * @param chain 过滤器链
     */
    public void pickCookieTokenInToHeader(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();

        // 遍历所有 Cookie 查找 Token
        for (Map.Entry<String, List<HttpCookie>> entry : cookies.entrySet()) {
            String key = entry.getKey();
            // 不区分大小写匹配 Token 键名
            if (key.trim().toLowerCase().equals(TokenFilter.TOKEN_KEY)) {
                List<HttpCookie> value = entry.getValue();
                if (value != null && value.size() == 1) {
                    String token = value.get(0).getValue();
                    if (CommonStringUtil.isNotBlank(token)) {
                        // 将 Cookie 中的 Token 添加到请求头
                        ServerHttpRequest requestWithToken = exchange.getRequest().mutate()
                                .headers(httpHeaders -> refreshToken(httpHeaders, token))
                                .build();
                        ServerWebExchange build = exchange.mutate().request(requestWithToken).build();
                        chain.filter(build);
                    }
                }
                break;
            }
        }
    }

    /**
     * 判断请求是否需要执行该过滤器
     * <p>
     * 基于 ACL（访问控制列表）进行判断，逻辑如下：
     * <ol>
     *   <li>首先检查是否存在调试标识请求头，不存在则跳过过滤</li>
     *   <li>检查请求 URL 是否在黑名单中，如果在则必须过滤</li>
     *   <li>检查请求 URL 是否在白名单中，如果在则跳过过滤</li>
     *   <li>默认情况下需要执行过滤</li>
     * </ol>
     *
     * @param exchange 服务器Web交换机，包含请求和响应信息
     * @return true-需要执行过滤，false-跳过该过滤器
     */
    public boolean shouldFilter(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        // 检查是否存在调试标识
        if (CommonStringUtil.isNotBlank(request.getHeaders().getFirst(TokenFilter.PRINT_FLAG))) {
            return false;
        }
        String url = exchange.getRequest().getURI().getPath();

        // 检查黑名单：匹配任意黑名单规则则必须过滤
        for (AclProvider aclProvider : aclProviderList) {
            for (String blackUrl : aclProvider.blackList()) {
                if (url.contains(blackUrl)) {
                    return true;
                }
            }
        }

        // 检查白名单：匹配任意白名单规则则跳过过滤
        for (AclProvider aclProvider : aclProviderList) {
            for (String whiteUrl : aclProvider.whiteList()) {
                if (url.contains(whiteUrl)) {
                    return false;
                }
            }
        }
        // 默认需要过滤
        return true;
    }

    /**
     * Token 处理器抽象基类
     * <p>
     * 定义了 Token 处理器的标准接口，支持多种 Token 验证策略
     * <p>
     * 实现类需要：
     * <ul>
     *   <li>实现 {@link #handle} 方法处理具体的验证逻辑</li>
     *   <li>实现 {@link #getHandlerBeanName} 方法返回处理器标识</li>
     * </ul>
     */
    public abstract static class TokenHandler {
        /**
         * 处理 Token 验证逻辑
         *
         * @param exchange 服务器Web交换机
         * @param chain 过滤器链
         * @return 异步完成信号
         */
        public abstract Mono<Void> handle(ServerWebExchange exchange, GatewayFilterChain chain);

        /**
         * 获取处理器的 Bean 名称
         * <p>
         * 用于根据类型查找对应的处理器实现
         *
         * @return 处理器名称（如：normalTokenHandler、workFlowTokenHandler）
         */
        public abstract String getHandlerBeanName();
    }

    /**
     * Token 处理器类型常量定义
     * <p>
     * 定义了系统支持的各种 Token 处理器类型标识
     */
    public static class TokenHandlerType {
        /**
         * 普通 Token 处理器
         * <p>
         * 用于常规的 Token 验证场景
         */
        public final static String NORMAL = "normalTokenHandler";

        /**
         * 工作流 Token 处理器
         * <p>
         * 用于工作流相关的特殊 Token 验证场景
         */
        public final static String WORKFLOW = "workFlowTokenHandler";
    }

}
