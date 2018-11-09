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

package com.pubutech.example.aop.config;

import com.pubutech.example.aop.interfacetest.BusinessImpl;
import com.pubutech.example.aop.interfacetest.Dancer;
import com.pubutech.example.aop.interfacetest.SkillImpl;
import com.pubutech.example.aop.thinking.TestMapper;
import com.pubutech.example.aop.thinking.TestServiceImpl;
import com.pubutech.example.patterns.factory.Computer;
import com.pubutech.example.patterns.factory.ComputerParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/4
 * @since 1.0
 */
@Configuration
public class TestConfig {

    @Bean
    public TestServiceImpl serviceImpl(){
        return new TestServiceImpl();
    }

    @Bean
    public TestMapper mapper(){
        return new TestMapper();
    }

    @Bean
    public BusinessImpl business(){
        return new BusinessImpl();
    }

    @Bean
    public Dancer dancer(){
        return new Dancer();
    }

    @Bean
    public SkillImpl skill(){return new SkillImpl();}

    @Bean
    public Computer.Builder computerBuilder(){
        return new Computer.Builder(new ComputerParams.ComputerParamsBuilder().setMemorySize(16)
                .setCpuCore(8)
                .setHardDiskSize(2000)
                .setMainBoardBrand("dell")
                .setChassisBrand("aoc").build());
    }
}
