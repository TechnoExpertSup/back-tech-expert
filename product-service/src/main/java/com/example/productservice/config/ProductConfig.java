package com.example.productservice.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper().registerModule(new RecordModule());
    }
}
