package com.iotinall.framework.configuration;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
public class ScheduleConfiguration implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(8,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build());
        taskRegistrar.setScheduler(executorService);
    }
}
