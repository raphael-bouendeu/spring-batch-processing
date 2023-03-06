package com.itbcafrica.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan({"com.itbcafrica.springbatch.config",
        "com.itbcafrica.springbatch.service",
        "com.itbcafrica.springbatch.listener",
        "com.itbcafrica.springbatch.reader",
        "com.itbcafrica.springbatch.controller",
        "com.itbcafrica.springbatch.writer",
        "com.itbcafrica.springbatch.processor"})
@EnableAsync
@EnableScheduling
public class SpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

}
