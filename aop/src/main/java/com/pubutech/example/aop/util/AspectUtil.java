/*
 * SC License
 *
 * Copyright (c)  SongChao 2018 .
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copyof this software and associated documentation files (the "Software").
 *
 * This software is only used for learning and communication.
 * Without permission,please do not use the software illegally.
 */

package com.pubutech.example.aop.util;

import javassist.*;

import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/4
 * @since 1.0
 */
public class AspectUtil {

    /**
     * 获取当前切面执行的方法所在的class
     *
     * @param point
     *         当前切面执行的方法
     */
    public static String getClassName(ProceedingJoinPoint point) {
        return point.getTarget().getClass().getName().replaceAll("\\.", "_");
    }

    /**
     * 获取当前切面执行的方法的方法名
     *
     * @param point
     *         当前切面执行的方法
     * @throws NoSuchMethodException
     */
    public static Method getMethod(ProceedingJoinPoint point) throws NoSuchMethodException {
        Signature sig = point.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        Object target = point.getTarget();
        return target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
    }

    /**
     * @Description 获取字段名和字段值
     * @return Map<String,Object>
     */
    public static Map<String,Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws Exception {
        Map<String,Object > map=new HashMap<String,Object>();


        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc;
        cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++){
            String key = attr.variableName(i + pos);
            map.put( key,args[i]);//paramNames即参数名
        }
        return map;
    }

}
