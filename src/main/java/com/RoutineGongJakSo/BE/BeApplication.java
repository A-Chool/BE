package com.RoutineGongJakSo.BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeApplication.class, args);

    }
//    @PostConstruct
//    public void started() { TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")); }
}
