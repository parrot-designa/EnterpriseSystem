package com.enterprisesystem.babycommon.config;

import com.enterprisesystem.babycommon.dao.RedisDao;
import com.enterprisesystem.babycommon.helpers.SequenceProducerHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@ConditionalOnClass(value = {RedisProperties.class})
public class BatchRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisDao redisDao(StringRedisTemplate redisTemplate){
        return new RedisDao(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public SequenceProducerHelper sequenceProducerHelper(RedisDao redisDao){
        return new SequenceProducerHelper(redisDao);
    }
}
