package net.lsun.bbs171;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("net.lsun.bbs171.repository")
public class Bbs171Application {

    public static void main(String[] args) {
        SpringApplication.run(Bbs171Application.class, args);
    }

}
