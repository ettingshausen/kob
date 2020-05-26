package com.ke.schedule.server.processor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * SpringBoot 启动类
 *
 * @Author: zhaoyuguang
 * @Date: 2018/9/12 下午4:40
 */
@EntityScan("com.ke")
@EnableJpaRepositories(basePackages = "com.ke")
@SpringBootApplication(scanBasePackages = {"com.ke"})
public class BootProcessor implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BootProcessor.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("==================starting processor==================");
        System.out.println("==================starting processor==================");
        System.out.println("==================starting processor==================");
    }
}