package cn.longyu.promotion;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
//开启定时任务
@EnableScheduling
//开启异步任务
@EnableAsync
@Slf4j
@SpringBootApplication
@MapperScan("cn.longyu.promotion.mapper")
public class PromotionApplication {
    public static void main(String[] args) {

        SpringApplication.run(PromotionApplication.class);


    }
}

