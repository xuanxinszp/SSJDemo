package com.xuanxinszp.ssjdemo.common.util;

import com.google.common.collect.Lists;
import com.xuanxinszp.ssjdemo.common.annotation.IgnoreParam;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 *  Aop帮助类
 * @date 2018/6/8
 */
public class AopUtil {
    
    private static Logger logger = LoggerFactory.getLogger(AopUtil.class);
    
    /**
     * getServiceMthodParams:获取json格式的参数. <br/>
     * @author lcma
     * @param joinPoint
     * @return
     * @throws Exception
     * @since JDK 1.7
     */
    public static String getServiceMthodParams(JoinPoint joinPoint)  {
        try {
            List<Object> argumentList = Lists.newArrayList();

            Method method = getMethod(joinPoint);
            IgnoreParam methodIgnoreParam = method.getDeclaredAnnotation(IgnoreParam.class);
            if (!isIgnoreParam(methodIgnoreParam)) {
                return null;
            }

            Parameter[] parameters = method.getParameters();

            Object[] arguments = joinPoint.getArgs();
            if (null != arguments && arguments.length > 0) {
                for (int i = 0; i < arguments.length; i++) {
                    // 先判断，是否有SystemLogIgnoreParam 注解
                    Parameter parameter = parameters[i];
                    IgnoreParam ingoreParam = parameter.getAnnotation(IgnoreParam.class);
                    Object obj = arguments[i];
                    if (isIgnoreParam(ingoreParam)) {
                        argumentList.add(obj);
                    }
                }
            }
            return CollectionUtil.isNil(argumentList) ? null : JsonUtil.beanToJson(argumentList);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return null;
        }
    }


    /**
     * 获取方法名称
     * @param joinPoint
     * @return
     */
    public static Method getMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method;
    }

    private static boolean isIgnoreParam(IgnoreParam ignoreParam) {
        return null == ignoreParam || !ignoreParam.value();
    }

}
