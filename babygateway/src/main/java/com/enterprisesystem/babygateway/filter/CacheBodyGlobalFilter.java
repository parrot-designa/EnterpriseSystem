package com.enterprisesystem.babygateway.filter;

import com.enterprisesystem.babygateway.util.GatewayBodyUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 请求体缓存全局过滤器
 *
 * <p>此过滤器用于在 Spring Cloud Gateway 网关中缓存请求体，解决请求体只能读取一次的问题。</p>
 *
 * <p>核心作用：</p>
 * <ul>
 *   <li>在所有过滤器执行之前，预先读取并缓存请求体数据</li>
 *   <li>使用装饰器模式替换原始请求，支持后续过滤器多次读取</li>
 *   <li>确保日志记录、参数验证、签名验证等过滤器都能读取到完整的请求体</li>
 * </ul>
 *
 * <p>为什么需要这个过滤器？</p>
 * <ol>
 *   <li>响应式编程中，请求体是流式数据，只能读取一次</li>
 *   <li>第一个过滤器读取后，后续过滤器就无法再读取</li>
 *   <li>通过缓存机制，让请求体可以被多次读取</li>
 * </ol>
 *
 * <p>执行优先级：</p>
 * <ul>
 *   <li>设置为最高优先级（HIGHEST_PRECEDENCE）</li>
 *   <li>确保在所有其他过滤器之前执行</li>
 *   <li>先缓存请求体，再执行其他过滤器的业务逻辑</li>
 * </ul>
 *
 * <p>工作流程：</p>
 * <ol>
 *   <li>检查请求头，判断是否包含需要缓存的请求体</li>
 *   <li>如果需要缓存，读取完整的请求体数据到字节数组</li>
 *   <li>创建装饰器，重写 getBody() 方法返回缓存的数据</li>
 *   <li>传递装饰后的请求给后续过滤器</li>
 * </ol>
 *
 * @see GatewayBodyUtils#checkBodyHeaders(ServerHttpRequest)
 */
@Component
public class CacheBodyGlobalFilter implements GlobalFilter, Ordered {

    /**
     * 过滤器主方法：拦截所有请求，缓存请求体
     *
     * <p>此方法会对每个经过网关的请求进行处理。</p>
     *
     * <p>处理逻辑：</p>
     * <ol>
     *   <li>获取当前请求对象</li>
     *   <li>检查请求是否包含需要缓存的请求体（JSON 或表单数据）</li>
     *   <li>如果需要缓存，调用 readBody() 方法读取并缓存请求体</li>
     *   <li>如果不需要缓存（如 GET 请求、文件上传），直接放行</li>
     * </ol>
     *
     * @param exchange 服务器 Web 交换对象，包含请求和响应
     * @param chain 过滤器链，用于传递请求到下一个过滤器
     * @return Mono<Void> 响应式异步结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        // 获取当前请求对象
        ServerHttpRequest request = exchange.getRequest();

//        // 检查请求是否包含需要缓存的请求体（JSON 或表单）
//        if(GatewayBodyUtils.checkBodyHeaders(request)){
//            // 需要缓存，读取并缓存请求体
//            return readBody(exchange, chain);
//        }

        // 不需要缓存，直接传递到下一个过滤器
        return chain.filter(exchange);
    }

    /**
     * 读取并缓存请求体
     *
     * <p>此方法是核心缓存逻辑，将请求体数据读取到内存中，并创建装饰器支持多次读取。</p>
     *
     * <p>工作流程：</p>
     * <ol>
     *   <li><b>读取请求体</b>：使用 DataBufferUtils.join() 将多个 DataBuffer 合并成一个</li>
     *   <li><b>转换为字节数组</b>：将 DataBuffer 的内容读取到 byte[] 数组中</li>
     *   <li><b>释放原始 DataBuffer</b>：释放原始的 DataBuffer，避免内存泄漏</li>
     *   <li><b>创建缓存的 Flux</b>：使用 Flux.defer() 延迟创建，每次订阅时返回新的 DataBuffer</li>
     *   <li><b>创建装饰器</b>：重写 getBody() 方法，返回缓存的 Flux</li>
     *   <li><b>替换请求</b>：用装饰后的请求替换原始请求</li>
     *   <li><b>传递到下一个过滤器</b>：继续执行过滤器链</li>
     * </ol>
     *
     * <p>为什么使用 Flux.defer()？</p>
     * <ul>
     *   <li>延迟创建：每次订阅时才创建 DataBuffer，而不是提前创建</li>
     *   <li>支持多次订阅：每个订阅者都会得到一个新的 DataBuffer</li>
     *   <li>避免数据竞争：多个过滤器同时读取时不会冲突</li>
     * </ul>
     *
     * <p>为什么使用 DataBufferUtils.retain()？</p>
     * <ul>
     *   <li>增加引用计数：防止 DataBuffer 被过早回收</li>
     *   <li>确保数据可用：在多个订阅者使用时保持数据有效</li>
     * </ul>
     *
     * @param exchange 服务器 Web 交换对象
     * @param chain 过滤器链
     * @return Mono<Void> 响应式异步结果
     */
    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain){
        // 1. 合并所有 DataBuffer（请求体可能被分成多个 DataBuffer）
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            // 2. 将 DataBuffer 内容读取到字节数组
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);

            // 3. 释放原始 DataBuffer（避免内存泄漏）
            DataBufferUtils.release(dataBuffer);

            // 4. 创建缓存的 Flux，每次订阅时返回新的 DataBuffer
            Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
               // 从字节数组创建新的 DataBuffer
               DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
               // 增加引用计数，防止被过早回收
               DataBufferUtils.retain(buffer);
               return Mono.just(buffer);
            });

            // 5. 创建请求装饰器，重写 getBody() 方法返回缓存的数据
            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()){
                @Override
                public Flux<DataBuffer> getBody(){
                    // 返回缓存的 Flux，支持多次读取
                    return cachedFlux;
                }
            };

            // 6. 用装饰后的请求替换原始请求，创建新的 Exchange
            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

            // 7. 继续执行过滤器链，传递装饰后的 Exchange
            return chain.filter(mutatedExchange);
        });
    }

    /**
     * 获取过滤器执行顺序
     *
     * <p>返回最高优先级，确保此过滤器在所有其他过滤器之前执行。</p>
     *
     * <p>为什么设置最高优先级？</p>
     * <ul>
     *   <li>先缓存请求体，后续的过滤器才能读取到完整的请求体数据</li>
     *   <li>避免其他过滤器提前读取请求体导致数据被消耗</li>
     *   <li>确保缓存逻辑在业务逻辑之前执行</li>
     * </ul>
     *
     * @return 最高优先级（Integer.MIN_VALUE，值越小优先级越高）
     */
    @Override
    public int getOrder(){
        // 返回最高优先级，确保此过滤器最先执行
        return HIGHEST_PRECEDENCE;
    }
}
