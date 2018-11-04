package com.water.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by pc on 2017/9/21.
 */
@SpringBootApplication
@ComponentScan({"com.water"})
public class StartupWaterApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartupWaterApplication.class, args);
    }
}
