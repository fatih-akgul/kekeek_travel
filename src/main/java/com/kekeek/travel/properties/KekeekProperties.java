package com.kekeek.travel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/kekeek.properties")
@ConfigurationProperties(prefix = "kekeek")
@Data
public class KekeekProperties {
    private ApiProperties api;
}
