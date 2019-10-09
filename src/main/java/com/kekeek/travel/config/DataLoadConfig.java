package com.kekeek.travel.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class DataLoadConfig {
    @Value("${DIR_INPUT}")
    private String contentDirectory;
}
