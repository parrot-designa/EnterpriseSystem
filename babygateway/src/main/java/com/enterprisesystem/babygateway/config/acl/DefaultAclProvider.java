package com.enterprisesystem.babygateway.config.acl;

import com.enterprisesystem.babygateway.config.AclProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 默认的访问控制列表（ACL）提供者
 *
 * <p>此类实现了 AclProvider 接口，用于提供网关的默认白名单配置。
 * 白名单中的路径不需要进行身份验证即可访问。</p>
 *
 * <p>使用场景：</p>
 * <ul>
 *   <li>登录接口：用户登录时还没有 Token，需要放行</li>
 *   <li>公开接口：如健康检查、静态资源等</li>
 *   <li>第三方回调：外部系统的回调接口</li>
 * </ul>
 *
 * @see AclProvider
 */
@Component
public class DefaultAclProvider implements AclProvider {

    /**
     * 默认白名单配置
     *
     * <p>多个路径使用逗号分隔，这些路径将跳过身份验证和授权检查。</p>
     *
     * <p>示例：</p>
     * <ul>
     *   <li>/login/user-login - 用户登录接口</li>
     *   <li>/api/public/** - 公开 API（支持通配符）</li>
     *   <li>/health - 健康检查接口</li>
     * </ul>
     */
    private final static String DEFAULT_WHITELIST = "/login/user-login";

    /**
     * 获取白名单路径列表
     *
     * <p>此方法将配置的白名单字符串按逗号分隔，并返回为路径列表。
     * 网关过滤器会检查请求路径是否在白名单中，如果在则放行。</p>
     *
     * <p>工作流程：</p>
     * <ol>
     *   <li>获取默认的白名单配置字符串</li>
     *   <li>按逗号分隔字符串得到路径数组</li>
     *   <li>转换为 List 返回</li>
     * </ol>
     *
     * @return 白名单路径列表，如 ["/login/user-login", "/api/public/**"]
     */
    @Override
    public List<String> whiteList(){
        // 获取白名单配置字符串
        String whitelistStr = DEFAULT_WHITELIST;

        // 按逗号分隔，得到路径数组
        String[] whitelist = whitelistStr.split(",");

        // 返回路径列表
        return Arrays.asList(whitelist);
    }
}
