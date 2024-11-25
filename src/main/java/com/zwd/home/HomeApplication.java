package com.zwd.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeApplication.class, args);
        System.out.println("欢迎回家~ o(*￣▽￣*)o：http://locahost:624");
    }
}
