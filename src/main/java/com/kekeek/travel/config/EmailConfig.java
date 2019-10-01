package com.kekeek.travel.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EmailConfig {
    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${EMAIL_FROM_ADDRESS}")
    private String fromAddress;

    @Value("${EMAIL_FROM_NAME}")
    private String fromName;

    @Value("${EMAIL_TO_ADDRESS}")
    private String toAddress;
}
