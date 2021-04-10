package com.xiangsy.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description:
 *
 */
@SpringBootApplication
@ComponentScan("com.xiangsy.demo")
@EnableAsync
public class DemoApp {

    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }

}
