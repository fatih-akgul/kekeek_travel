package com.kekeek.travel.properties;

import lombok.Data;

@Data
public class ApiProperties {
    private String baseUrl;

    public String getUrlSitePage(String sitePageIdentifier) {
        return baseUrl + "/site_pages/" + sitePageIdentifier;
    }
}
