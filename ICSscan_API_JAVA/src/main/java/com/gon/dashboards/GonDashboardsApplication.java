package com.gon.dashboards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GonDashboardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GonDashboardsApplication.class, args);
    }

}
