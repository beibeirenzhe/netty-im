package com.fyrt.fyrtwebsocketim;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.fyrt.fyrtwebsocketim.webSocket.mapper.**"})
public class FyrtwebsocketimApplication {

    public static void main(String[] args) {
        SpringApplication.run(FyrtwebsocketimApplication.class, args);
    }

}
