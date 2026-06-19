package org.forestwizard.springdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import java.util.TimeZone;

@SpringBootApplication
@RestController
public class SprintDemoApplication {
    public static void main(String[] args) {
        System.out.println("JVM timezone: " + TimeZone.getDefault().getID());
        SpringApplication.run(SprintDemoApplication.class, args);
    }
}
