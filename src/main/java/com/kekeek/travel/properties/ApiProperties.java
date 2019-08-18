package com.kekeek.travel.properties;

import lombok.Data;

@Data
public class ApiProperties {
    private String baseUrl;

    public String getUrlForPage(String sitePageIdentifier) {
        return baseUrl + "/pages/" + sitePageIdentifier;
    }

    public String getUrlForContent(String pageIdentifier, String contentIdentifier) {
        return baseUrl + "/pages/" + pageIdentifier + "/contents/" + contentIdentifier;
    }
}
