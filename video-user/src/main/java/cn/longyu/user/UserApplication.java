package cn.longyu.user;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.longyu.user.mapper")
public class UserApplication {
    public static void main(String[] args) {

        SpringApplication.run(UserApplication.class);
    }
}
