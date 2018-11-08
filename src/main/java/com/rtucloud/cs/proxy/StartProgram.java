package com.rtucloud.cs.proxy;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAdminServer
@MapperScan(basePackages = "com.rtucloud.cs.proxy.dao.repository")
public class StartProgram {

    public static void main(String[] args) {
        SpringApplication.run(StartProgram.class, args);
    }


}
