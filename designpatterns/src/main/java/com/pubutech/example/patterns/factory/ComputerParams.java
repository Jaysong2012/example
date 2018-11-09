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

package com.pubutech.example.patterns.factory;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SongChao
 * @version 1.0
 * @website https://github.com/Jaysong2012
 * @date 2018/9/13
 * @since 1.0
 */

@Data
//电脑组装清单
public class ComputerParams {

    public ComputerParams(){}

    public ComputerParams(int cpuCore,int memorySize,int hardDiskSize,String mainBoardBrand,String chassisBrand){
        this.cpuCore = cpuCore;
        this.memorySize = memorySize;
        this.hardDiskSize = hardDiskSize;
        this.mainBoardBrand = mainBoardBrand;
        this.chassisBrand = chassisBrand;
    }

    //机箱品牌
    private String chassisBrand;

    //CPU核心数
    private int cpuCore;

    //硬盘大小
    private int hardDiskSize;

    //主板平拍
    private String mainBoardBrand;

    //内存大小
    private int memorySize;

    public static class ComputerParamsBuilder{
        private final ComputerParams params;

        public ComputerParamsBuilder(){
            params = new ComputerParams(4,16,1000,"default","default");
        }

        public ComputerParamsBuilder setChassisBrand(String chassisBrand){
            this.params.setChassisBrand(chassisBrand);
            return this;
        }

        public ComputerParamsBuilder setCpuCore(int cpuCore){
            this.params.setCpuCore(cpuCore);
            return this;
        }

        public ComputerParamsBuilder setHardDiskSize(int hardDiskSize){
            this.params.setHardDiskSize(hardDiskSize);
            return this;
        }

        public ComputerParamsBuilder setMainBoardBrand(String mainBoardBrand){
            this.params.setMainBoardBrand(mainBoardBrand);
            return this;
        }

        public ComputerParamsBuilder setMemorySize(int memorySize){
            this.params.setMemorySize(memorySize);
            return this;
        }

        public ComputerParams build(){
            return params;
        }
    }

}
