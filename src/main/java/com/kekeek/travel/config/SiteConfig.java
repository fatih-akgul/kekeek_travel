package com.kekeek.travel.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class SiteConfig {
    @Value("${SITE_NAME}")
    private String siteName;

    @Value("${COUNTRY}")
    private String country;

    @Value("${BASE_IMAGE_URL}")
    private String baseImageUrl;

    public String getUrlPageImage(String pageIdentifier) {
        return baseImageUrl + "/pages/" + pageIdentifier.substring(0, 2) + "/" + pageIdentifier + ".jpg";
    }
}
