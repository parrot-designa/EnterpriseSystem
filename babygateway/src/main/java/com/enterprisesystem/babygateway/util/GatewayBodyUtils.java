package com.enterprisesystem.babygateway.util;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 网关请求体工具类
 *
 * <p>此工具类用于在 Spring Cloud Gateway 网关中读取和缓存请求体内容。</p>
 *
 * <p>核心问题：在响应式编程中，请求体（RequestBody）只能读取一次，读取后就会被消耗掉。
 * 这导致在网关的多个过滤器中无法重复读取请求体内容。</p>
 *
 * <p>解决方案：</p>
 * <ul>
 *   <li>使用装饰器模式缓存请求体数据</li>
 *   <li>支持多次读取同一个请求体</li>
 *   <li>支持 JSON 和表单提交的数据格式</li>
 * </ul>
 *
 * <p>使用场景：</p>
 * <ul>
 *   <li>日志记录：记录请求参数用于审计</li>
 *   <li>参数验证：在网关层验证请求参数</li>
 *   <li>参数修改：修改请求体内容后转发</li>
 *   <li>签名验证：验证请求签名</li>
 * </ul>
 */
public class GatewayBodyUtils {

    /**
     * 获取请求体内容
     *
     * <p>此方法会检查请求头，判断是否包含可读取的请求体数据。</p>
     *
     * <p>工作流程：</p>
     * <ol>
     *   <li>检查请求头，判断是否需要读取请求体（调用 checkBodyHeaders）</li>
     *   <li>如果需要读取，创建装饰器对象缓存请求体数据</li>
     *   <li>订阅响应式流，读取并解码请求体内容</li>
     *   <li>将解码后的字符串存储到 AtomicReference 中</li>
     *   <li>返回请求体字符串</li>
     * </ol>
     *
     * <p>注意事项：</p>
     * <ul>
     *   <li>⚠️ 此方法可能返回 null（如果请求不包含请求体）</li>
     *   <li>⚠️ 在响应式流中， requestBody.get() 可能在订阅完成前返回空字符串</li>
     *   <li>建议使用同步方式或添加延迟确保数据读取完成</li>
     * </ul>
     *
     * @param req 服务器请求对象（响应式）
     * @return 请求体字符串（JSON 或表单格式），如果请求不包含请求体则返回 null
     * @see #checkBodyHeaders(ServerHttpRequest)
     * @see RecorderServerHttpRequestDecorator
     */
    public static String getBody(ServerHttpRequest req){
        // 检查请求头，判断是否需要读取请求体
        if(checkBodyHeaders(req)){
            // 使用 AtomicReference 在响应式流中存储字符串结果
            AtomicReference<String> requestBody = new AtomicReference<>("");

            // 创建装饰器，用于缓存请求体数据
            RecorderServerHttpRequestDecorator requestDecorator = new RecorderServerHttpRequestDecorator(req);

            // 获取请求体的响应式流
            Flux<DataBuffer> body = requestDecorator.getBody();

            // 订阅响应式流，读取并解码数据
            body.subscribe(buffer -> {
                // 将 DataBuffer（字节缓冲）解码为字符缓冲
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
                // 将解码后的字符串存储到 AtomicReference 中
                requestBody.set(charBuffer.toString());
            });

            // 返回请求体字符串（可能是空字符串，因为订阅是异步的）
            return requestBody.get();
        }
        return null;
    }

    /**
     * 检查请求头，判断是否包含需要读取的请求体
     *
     * <p>此方法通过检查 HTTP 请求头，判断请求是否包含可读取的请求体数据。</p>
     *
     * <p>检查条件：</p>
     * <ol>
     *   <li>Content-Length > 0：请求体长度大于 0</li>
     *   <li>Content-Type 不是 multipart/form-data：不支持文件上传</li>
     *   <li>Content-Type 是以下类型之一：</li>
     *   <ul>
     *     <li>application/json：JSON 数据</li>
     *     <li>application/json;charset=UTF-8：JSON 数据（UTF-8 编码）</li>
     *     <li>application/x-www-form-urlencoded：表单提交数据</li>
     *   </ul>
     * </ol>
     *
     * <p>为什么排除 multipart/form-data？</p>
     * <ul>
     *   <li>文件上传的请求体通常是二进制数据，不适合转换为字符串</li>
     *   <li>文件数据通常较大，不应在网关层读取</li>
     *   <li>文件上传不需要参数验证或日志记录</li>
     * </ul>
     *
     * @param req 服务器请求对象（响应式）
     * @return 如果请求包含可读取的请求体返回 true，否则返回 false
     */
    public static boolean checkBodyHeaders(ServerHttpRequest req){
        // 获取请求头
        HttpHeaders headers = req.getHeaders();
        // 获取 Content-Type（内容类型）
        MediaType contentType = headers.getContentType();
        // 获取 Content-Length（内容长度）
        long contentLength = headers.getContentLength();

        // 检查条件：
        // 1. 内容长度大于 0
        // 2. 不是 multipart/form-data（文件上传）
        if(contentLength > 0 && !MediaType.MULTIPART_FORM_DATA_VALUE.startsWith(contentType.toString())){
            // 检查内容类型是否为支持的格式（JSON 或表单）
            if(MediaType.APPLICATION_JSON.equals(contentType) ||
                    MediaType.APPLICATION_JSON_UTF8.equals(contentType)||
                    contentType.toString().contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            ){
                return true;
            }
        }
        return false;
    }

    /**
     * 请求装饰器 - 用于缓存请求体数据
     *
     * <p>此类继承自 ServerHttpRequestDecorator，采用装饰器模式。</p>
     *
     * <p>核心作用：</p>
     * <ul>
     *   <li>缓存请求体数据：将请求体 DataBuffer 存储到列表中</li>
     *   <li>支持多次读取：每次调用 getBody() 都返回数据的副本</li>
     *   <li>解决"只能读取一次"的问题</li>
     * </ul>
     *
     * <p>工作原理：</p>
     * <ol>
     *   <li>构造函数中读取原始请求的 body 数据</li>
     *   <li>将每个 DataBuffer 存储到 dataBuffers 列表中</li>
     *   <li>重写 getBody() 方法，返回数据的副本</li>
     *   <li>副本通过 copy() 方法创建，将 DataBuffer 的 ByteBuffer 包装成新的 DataBuffer</li>
     * </ol>
     *
     * <p>使用示例：</p>
     * <pre>{@code
     * // 创建装饰器，自动缓存请求体
     * RecorderServerHttpRequestDecorator decorator = new RecorderServerHttpRequestDecorator(request);
     *
     * // 第一次读取
     * decorator.getBody().subscribe(...);
     *
     * // 第二次读取（仍然可以读取到数据）
     * decorator.getBody().subscribe(...);
     * }</pre>
     */
    public static class RecorderServerHttpRequestDecorator extends ServerHttpRequestDecorator {
        /**
         * 缓存请求体数据的列表
         * 每个元素都是一个 DataBuffer（数据缓冲区）
         */
        private final List<DataBuffer> dataBuffers = new ArrayList<>();

        /**
         * 构造函数：创建装饰器并缓存请求体数据
         *
         * <p>在构造时，会立即读取原始请求的 body 数据并缓存。</p>
         *
         * @param delegate 被装饰的原始请求对象
         */
        public RecorderServerHttpRequestDecorator(ServerHttpRequest delegate){
            super(delegate);

            // 读取原始请求的 body，并缓存到列表中
            super.getBody().map(dataBuffer -> {
                // 将每个 DataBuffer 添加到缓存列表
                dataBuffers.add(dataBuffer);
                return dataBuffer;
            }).subscribe(); // 立即订阅，触发数据读取
        }

        /**
         * 重写 getBody() 方法，返回请求体数据的副本
         *
         * <p>原始方法只能读取一次，此方法通过返回副本支持多次读取。</p>
         *
         * @return 响应式流，每个元素是一个 DataBuffer 副本
         */
        @Override
        public Flux<DataBuffer> getBody(){
            return copy();
        }

        /**
         * 创建请求体数据的副本
         *
         * <p>此方法将缓存的 DataBuffer 数据复制一份，返回新的响应式流。</p>
         *
         * <p>复制原理：</p>
         * <ul>
         *   <li>遍历缓存的 dataBuffers 列表</li>
         *   <li>将每个 DataBuffer 的 ByteBuffer 包装成新的 DataBuffer</li>
         *   <li>返回包含新 DataBuffer 的 Flux 流</li>
         * </ul>
         *
         * @return 响应式流，每个元素是一个 DataBuffer 副本
         */
        public Flux<DataBuffer> copy(){
            return Flux.fromIterable(dataBuffers)
                    .map(buf -> buf.factory().wrap(buf.asByteBuffer()));
        }
    }
}
