package com.ke.schedule.server.console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * SpringBoot 启动类
 *
 * @Author: zhaoyuguang
 * @Date: 2018/9/12 上午11:19
 */
@EntityScan("com.ke")
@EnableJpaRepositories(basePackages = "com.ke")
@SpringBootApplication(scanBasePackages = {"com.ke"})
@ServletComponentScan
public class BootConsole implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BootConsole.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("==================starting console==================");
        System.out.println("==================starting console==================");
        System.out.println("==================starting console==================");
    }
}
