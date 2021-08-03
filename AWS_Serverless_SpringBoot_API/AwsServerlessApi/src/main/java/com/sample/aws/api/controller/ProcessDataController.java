package com.sample.aws.api.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProcessDataController {

   @Bean
    public Function<String, String> processdata() {
        return val -> "Hello " + val;
    }

    @Bean
    public Function<String, String> uppercase() {
        return value -> value.toUpperCase();
    }
}
