package com.enterprisesystem.babycommon.aspect;

import com.enterprisesystem.babycommon.annotation.CommonExceptionHandler;
import com.enterprisesystem.babycommon.exception.SystemRuntimeException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommonExceptionAspect {

    @Pointcut("@annotation(com.enterprisesystem.babycommon.aspect.CommonExceptionHandler)")
    public void commonPointcut(){}

    @AfterThrowing(pointcut = "commonPointcut() && @annotation(annon)",throwing = "e")
    public void handle(JoinPoint joinPoint, CommonExceptionHandler annon,Exception e){
        int errorCode; // 错误代码
        String errorInfo; // 错误信息

        throw (SystemRuntimeException)e;
    }
}
