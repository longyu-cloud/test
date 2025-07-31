package cn.longyu.remark;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.longyu.remark.mapper")
public class RemarkApplication {

    public static void main(String[] args) {

        SpringApplication.run(RemarkApplication.class);

    }
}
