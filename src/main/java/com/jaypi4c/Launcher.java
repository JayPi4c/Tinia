package com.jaypi4c;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class Launcher {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(Launcher.class, args);
        log.info("Application started");

        Pipeline pipeline = app.getBean(Pipeline.class);
        pipeline.start();

        log.info("Application finished");
    }


}
