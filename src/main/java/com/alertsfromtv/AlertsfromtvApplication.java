package com.alertsfromtv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class AlertsfromtvApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertsfromtvApplication.class, args);
    }

}
