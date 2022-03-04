package com.iotinall.framework.configuration;

import com.iotinall.framework.modules.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;

@Slf4j
@Configuration
public class InitConfiguration implements ApplicationRunner {

    @Resource
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //让子线程能获取到登录信息
        SecurityContextHolder.setStrategyName("MODE_INHERITABLETHREADLOCAL");
        userService.initSupperUser();
    }
}
