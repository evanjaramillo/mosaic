package com.mosaic.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MosaicServer {

    public static void main(String[] args) {
        SpringApplication.run(MosaicServer.class, args);
    }
}
