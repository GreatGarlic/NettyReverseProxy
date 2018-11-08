package com.rtucloud.cs.proxy;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAdminServer
public class StartProgram {

    public static void main(String[] args) {
        SpringApplication.run(StartProgram.class, args);
    }


}
