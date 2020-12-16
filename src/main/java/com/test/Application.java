package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.test.service.CalculationService;
import com.test.service.CalculationServiceImpl;
import com.test.service.CollectorService;
import com.test.service.CollectorServiceImpl;
import com.test.service.QueryService;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CalculationService calculationService() {
        return new CalculationServiceImpl();
    }

    @Bean
    CollectorService collectorService() {
        return new CollectorServiceImpl();
    }

    @Bean
    QueryService queryService() {
        return new QueryService();
    }
}
