package com.enterprisesystem.babycommon.helpers;

import com.enterprisesystem.babycommon.dao.RedisDao;

public class SequenceProducerHelper {
    private final RedisDao redisDao;
    private static final String INNER_UNIQUE_SEQUENCE = "inner_unique_sequence";

    public SequenceProducerHelper(RedisDao redisDao){
        this.redisDao = redisDao;
    }

    public long getUniqueSequence(){
        try{
            return redisDao.incr(INNER_UNIQUE_SEQUENCE);
        }catch(Exception ex){
            String errMsg = "[ERR217] 生成唯一序列号失败："+ex.getMessage();
            throw new RuntimeException(errMsg);
        }
    }
}
