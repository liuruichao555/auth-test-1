package com.cdd.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * TestApplication
 *
 * @author liuruichao
 * Created on 2018/7/2 20:45
 */
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
