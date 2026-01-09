package com.enterprisesystem.babygateway.filter;

import com.enterprisesystem.babycommon.authentication.TokenConstants;
import com.enterprisesystem.babycommon.utils.CommonStringUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

public class TokenHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder(){
        return 1;
    }

//    public void fillTokenHeader(Map<String,String> headers,String token){
//        setHeader(headers, );
//    }

    private void setHeader(Map<String,String> headers,String key,Object value){
        if(value == null){
            value = "";
        }
        headers.put(key, String.valueOf(value));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        String token = exchange.getRequest().getHeaders().getFirst(TokenConstants.HEADER_TOKEN);
        if(CommonStringUtil.isNotBlank(token)){
        }
        return chain.filter(exchange);
    }


}
