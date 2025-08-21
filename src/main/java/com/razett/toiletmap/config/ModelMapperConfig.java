package com.razett.toiletmap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ObjectMapper Bean 등록 Config
 *
 * @since 2025-05-25 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
