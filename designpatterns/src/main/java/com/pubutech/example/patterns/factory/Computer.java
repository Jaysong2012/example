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

import com.pubutech.example.patterns.factory.bean.*;
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
public class Computer {

    //内存
    private Memory memory;

    private CPU cpu;

    //主板
    private MainBoard mainBoard;

    //硬盘
    private HardDisk hardDisk;

    //机箱
    private Chassis chassis;

    public Computer(Memory memory,CPU cpu,HardDisk hardDisk,MainBoard mainBoard,Chassis chassis){
        this.memory = memory;
        this.cpu = cpu;
        this.hardDisk = hardDisk;
        this.mainBoard = mainBoard;
        this.chassis = chassis;
    }

    @Data
    //Builder模式模拟电脑生产线
    public static class Builder{

        private final ComputerParams params;
        private final Computer computer;

        //工厂接收客户组装清单生产
        public Builder(ComputerParams params){
            System.err.println("接收电脑组装清单");
            if(params == null){
                this.params = new ComputerParams.ComputerParamsBuilder().build();
            }else{
                this.params = params;
            }
            this.computer = new Computer(null,null,null,null,null);
        }


        public Builder buildMemory(){
            System.err.println("建造了一个"+this.params.getMemorySize()+"G内存的Memory");
            this.computer.setMemory(new Memory(this.params.getMemorySize()));
            return this;
        }

        public Builder buildCpu(){
            System.err.println("建造了一个"+this.params.getCpuCore()+"核的CPU");
            this.computer.setCpu(new CPU(this.params.getCpuCore()));
            return this;
        }

        public Builder buildHardDisk(){
            System.err.println("建造了一个"+this.params.getHardDiskSize()+"G空间的硬盘");
            this.computer.setHardDisk(new HardDisk(this.params.getHardDiskSize()));
            return this;
        }

        public Builder buildMainBoard(){
            System.err.println("建造了一个"+this.params.getMainBoardBrand()+"品牌的主板");
            this.computer.setMainBoard(new MainBoard(this.params.getMainBoardBrand()));
            return this;
        }

        public Builder buildChassis(){
            System.err.println("建造了一个"+this.params.getChassisBrand()+"品牌的机箱");
            this.computer.setChassis(new Chassis(this.params.getChassisBrand()));
            return this;
        }

        public Computer build(){

            System.err.println("建造一台"+computer.getCpu().getCore()
                    +"核CPU "+computer.getMemory().getSize()
                    +"G内存 "+computer.getHardDisk().getSize()
                    +"G磁盘 "+computer.getMainBoard().getBrand()
                    +"品牌主板 "+computer.getChassis().getBrand()
                    +"品牌机箱 的电脑");
            return computer;
        }
    }

}
