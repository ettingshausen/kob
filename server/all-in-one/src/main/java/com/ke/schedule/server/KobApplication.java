package com.ke.schedule.server;

import com.ke.schedule.server.core.model.db.User;
import com.ke.schedule.server.core.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.Resource;

/**
 * @author ettingshausen
 */
@Slf4j
@EntityScan("com.ke")
@EnableJpaRepositories(basePackages = "com.ke.schedule.server.core.repository")
@SpringBootApplication
@ServletComponentScan
@ComponentScan(excludeFilters = {@ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = {com.ke.schedule.server.processor.BootProcessor.class,
                com.ke.schedule.server.console.BootConsole.class})
})
public class KobApplication implements CommandLineRunner {

    @Resource
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(KobApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("==================starting Kob==================");
        log.info("==================starting Kob==================");
        log.info("==================starting Kob==================");

        User user = userRepository.findByCode("xiaoming");
        if (user == null) {
            log.info("================starting init data==============");
            user = new User();
            user.setCode("xiaoming");
            user.setName("小明");
            user.setPwd("xiaoming");
            user.setConfiguration("{\"mail\":\"xiaoming@ke.com\"}");
            userRepository.save(user);
        }

    }
}
