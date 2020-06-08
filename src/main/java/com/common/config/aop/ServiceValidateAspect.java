package com.common.config.aop;

import com.common.annotation.ParamsCheck;
import com.common.exception.ParamsCheckException;
import com.common.validation.BeanValidateUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description: service 层入参校验切点
 */
@Aspect
@Component
public class ServiceValidateAspect {

    /**
     * service 层切点
     */
    @Pointcut("@annotation(com.common.annotation.ParamsCheck)")
    public void servicePointcut() {
    }

    /**
     * service 层入参校验
     *
     * @param joinPoint 切点
     * @return
     * @throws Throwable
     */
    @Around(value = "servicePointcut()")
    public Object serviceParamCheckAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 判断是否需要校验
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ParamsCheck paramsCheckAnnotation = method.getAnnotation(ParamsCheck.class);
        if (paramsCheckAnnotation != null && paramsCheckAnnotation.ignore()) {
            return joinPoint.proceed();
        }
        /**
         * 参数校验
         */
        Object[] objects = joinPoint.getArgs();
        for (Object arg : objects) {
            if (arg == null) {
                break;
            }
            // 参数校验
            String validResult = BeanValidateUtil.validate(arg);
            if (validResult != null && validResult.length() > 0) {
                throw new ParamsCheckException(validResult);
            }
        }
        return joinPoint.proceed();
    }
}
