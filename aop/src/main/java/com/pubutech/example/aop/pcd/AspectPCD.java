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

package com.pubutech.example.aop.pcd;

import com.pubutech.example.aop.args.Message;
import com.pubutech.example.aop.interfacetest.ISkill;
import com.pubutech.example.aop.interfacetest.SkillImpl;
import com.pubutech.example.aop.util.AspectUtil;
import com.pubutech.example.patterns.factory.Computer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/4
 * @since 1.0
 */

@Aspect
@Component
public class AspectPCD {

    /*
    在一个方法执行连接点代表了任意public方法的执行时匹配
     */
    @Pointcut("execution (public * * (..)) ")
    private void anyPublicOperation(){}

    /*
    任何以do开头的函数
     */
    @Pointcut("execution (* do* (..)) ")
    private void anyDoOperation(){}

    /*
    Package下的任意连接点(方法)执行时匹配
    */
    @Pointcut("within (com.pubutech.example.aop.thinking.*)")
    private void inPackage(){}

    /*
    Package下或者子包下的任意连接点(方法)执行时匹配
     */
    @Pointcut("within (com.pubutech.example.aop.thinking..*)")
    private void inPackages(){}

    /*
    Package下或者子包下的任意public连接点(任意public方法)执行时匹配
     */
    @Pointcut("inPackages() && anyPublicOperation() ")
    private void inPackageAnyPublicOPeration(){}

    /*
    Package下或者子包下的任意public连接点(任意public方法)执行时匹配
    实际上 这里告诉大家 within  target args 都没有我们都可以用execution 直接做到
    */
    @Pointcut("execution (public * com.pubutech.example.aop.thinking..*.*(..)) ")
    private void inPackageAnyPublicOPerationInexecution(){}


    /*
    Package下或者子包下的任意连接点(方法)执行时匹配
    */
    @Pointcut("execution(* com.pubutech.example.aop.thinking..*.*(..))")
    public void executioniInPackagesPointCut() {}

    /*
    Package下的任意连接点(方法)执行时匹配
    */
    @Pointcut("execution(* com.pubutech.example.aop.thinking.*.*(..))")
    public void executioniInPackagePointCut() {}

    /*
    实现接口的所有实体对象的所有方法执行
    */
    @Pointcut("target (com.pubutech.example.aop.interfacetest.IBussiness)")
    public void targetPointCut(){}

    /*
    接口的所有代理对象的所有方法执行，一般情况下和target一样，高深用法后面演示
    */
    @Pointcut("this (com.pubutech.example.aop.interfacetest.IBussiness)")
    public void thisPointCut(){}

    /*
    精确拿到方法执行传递的参数
    实际上  args参数我们可以用JAVA反射拿到，所以不用也是可以的，后面Around有案例
     */
    @Pointcut("execution(* com.pubutech.example.aop.interfacetest.BusinessImpl.doException(..)) && args(msg)")
    public void argsPointCut(Message msg){}

    /*
    类级别的注解 里面所有的函数执行时
    */
    @Pointcut("@within(org.springframework.stereotype.Component)")
    public void withinPointCut(){}

    /*
    函数传递的参数使用的注解对象
    RequestBody Controller里面的Mapping方法里面请求参数是JSON格式的注解，帮助大家理解函数参数注解
    @PostMapping(value = "/add")
    public ObjectResponse add(@RequestBody UserEntity user) {
     */
    //@Pointcut("@args(org.springframework.web.bind.annotation.RequestBody)")
    public void aArgsPointCut(){}

    /*
    方法的注解所使用的
    Transactional   方法事务执行的注解，这边只是一个案例，帮助大家理解一下方法注解
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void addRoleResources(Long roleId, String resourcesId) {
     */
    //@Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void annotationPointcut() {
    }

    /*
    Spring 特有的bean操作
     */
    @Pointcut("bean(serviceImpl)")
    public void beanPointCut(){}


    @Before("inPackageAnyPublicOPerationInexecution()")
    public void before(JoinPoint point) {
        System.out.println("切入成功");
    }

    @AfterThrowing(pointcut = "argsPointCut(msg)",throwing = "ex")
    public void AfterThrowing(Message msg, Exception ex){
        System.out.println("这里演示抛出异常增强和拿到切入方法参数"+msg.getMessage());
        System.out.println(ex);
    }

    @AfterReturning(pointcut = "thisPointCut()",returning = "result")
    public void afterReturn(Object result){
        System.out.println("AfterReturning 拿到返回的结果 "+result);
    }

    @After("beanPointCut()")
    public void after(JoinPoint point) {
        System.out.println("后置切入成功");
    }

    @Around("execution(* com.pubutech.example.aop.interfacetest.BusinessImpl.doException(..))")
    private Object around(ProceedingJoinPoint point){
        //执行业务之前，我们可以做到Before
        //doBefore

        //获取拦截方法类和方法
        String className = AspectUtil.getClassName(point);
        Method currentMethod = null;
        try {
            currentMethod = AspectUtil.getMethod(point);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println("className = "+className+" currentMethod = "+currentMethod.getName());
        Map<String,Object > nameAndArgs = null;
        //获取参数名称和值 是的 参数也可以获取
        try {
            nameAndArgs = AspectUtil.getFieldsName(this.getClass(), point.getTarget().getClass().getName(), currentMethod.getName(),point.getArgs());
        } catch (Exception e) {
            e.printStackTrace();
        }


        Object result = null;
        //执行业务
        try {
            //拿到执行结果
            result = point.proceed();
        } catch (Throwable throwable) {
            //拿到异常,我们可以处理异常afterThrow
            throwable.printStackTrace();
        }

        //拿到结果之后我们可以做到AfterReturn
        //DoAfterReturn

        return result;
        //doFinalAfter 这里是做不到的
    }


    // “....Human”后面的 “+” 号，表示只要是Human及其子类都可以添加新的方法
    @DeclareParents(value = "com.pubutech.example.aop.interfacetest.Human+", defaultImpl = SkillImpl.class)
    public ISkill skill;

    @AfterReturning(pointcut = "execution (* com.pubutech.example.patterns.factory..*.buildHardDisk(..))",returning = "result")
    public void afterBuildHardDisk(Object result){
        Computer.Builder builder = (Computer.Builder)result;
        System.out.println("把"+builder.getComputer().getHardDisk().getSize()+"G的硬盘预先放入我们自己的宣传视频");
    }

//    @After(" execution(* com.pubutech.example.patterns.factory..*.*(..))")
//    public void doSothing(JoinPoint point){
//        try {
//            MethodSignature msig = (MethodSignature)point.getSignature();
//            System.out.println("做些什么事呢"+point.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes()));
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//    }
}
