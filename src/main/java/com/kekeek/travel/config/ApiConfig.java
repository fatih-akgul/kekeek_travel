package com.kekeek.travel.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApiConfig {
    @Value("${API_SERVER}")
    private String apiServer;

    public String getUrlPages() {
        return apiServer + "/pages";
    }

    public String getUrlPage(String pageIdentifier) {
        return apiServer + "/pages/" + pageIdentifier;
    }

    public String getUrlContent(String pageIdentifier, String contentIdentifier) {
        return apiServer + "/pages/" + pageIdentifier + "/contents/" + contentIdentifier;
    }

    public String getUrlPageBreadcrumbs(String pageIdentifier) {
        return getUrlPage(pageIdentifier) + "/breadcrumbs";
    }

    public String getUrlPageParent(String pageIdentifier) {
        return getUrlPage(pageIdentifier) + "/parent";
    }

    public String getUrlPageChildArticles(String pageIdentifier) {
        return getUrlPage(pageIdentifier) + "/children";
    }

    public String getUrlPageChildPages(String pageIdentifier) {
        return getUrlPage(pageIdentifier) + "/pages";
    }

    public String getUrlAllArticles() {
        return getUrlPages() + "/articles";
    }

    public String getUrlTopNavPages() {
        return getUrlPages() + "/menu-pages";
    }

    public String getUrlEmails() {
        return apiServer + "/emails";
    }

    public String getUrlVisits() {
        return apiServer + "/visits";
    }

    public String getUrlTopVisits(int topVisitCount) {
        return getUrlVisits() + "/top/" + topVisitCount;
    }
}
