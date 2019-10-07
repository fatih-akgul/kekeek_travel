package com.kekeek.travel.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Data
@EnableScheduling
public class SiteConfig {
    @Value("${SITE_NAME}")
    private String siteName;

    @Value("${BASE_IMAGE_URL}")
    private String baseImageUrl;

    public String getUrlPageImage(String pageIdentifier) {
        return baseImageUrl + "/pages/" + pageIdentifier.substring(0, 2) + "/" + pageIdentifier + ".jpg";
    }
}
