package com.iotinall.framework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = {"com.iotinall.framework.modules.**.mapper", "com.gitee.sunchenbin.mybatis.actable.dao.*"})
@ComponentScan({"com.gitee.sunchenbin.mybatis.actable.manager.*", "com.iotinall.framework"})
public class MybatisPlusFrameworkApplication {
    public static void main(String[] args) {

        SpringApplication.run(MybatisPlusFrameworkApplication.class, args);
    }
}
