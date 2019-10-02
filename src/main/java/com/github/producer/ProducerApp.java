package com.github.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProducerApp {

    final static Logger LOG = LoggerFactory.getLogger(ProducerApp.class);

    public static void main(String[] args) {

        try {
            SpringApplication.run(ProducerApp.class, args);
            LOG.info("----------- Producer application is up ! -----------");
        } catch (Exception e) {
            LOG.error("Encountered exception while starting application", e);
        }
    }
}
