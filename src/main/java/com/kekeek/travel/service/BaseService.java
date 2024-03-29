package com.kekeek.travel.service;

import com.kekeek.travel.config.ApiConfig;
import com.kekeek.travel.config.SiteConfig;
import com.kekeek.travel.model.BaseModel;
import com.kekeek.travel.model.SitePage;
import com.kekeek.travel.model.Visit;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@CacheConfig(cacheNames={"pages"})
abstract class BaseService {

    ApiConfig apiConfig;
    SiteConfig siteConfig;
    RestTemplate restTemplate;

    Map<String, Object> getMetaFields(SitePage page) {
        return getMetaFields(page, null);
    }

    Map<String, Object> getMetaFields(String pageIdentifier) {
        SitePage page = getPage(pageIdentifier);
        return getMetaFields(page);
    }

    Map<String, Object> getMetaFields(SitePage page, SitePage parentArticle) {
        Set<String> keywords = new HashSet<>(page.getKeywords());
        if (parentArticle != null) {
            keywords.addAll(parentArticle.getKeywords());
        }

        Map<String, Object> fields = new HashMap<>();
        fields.put("pageTitle", page.getDescription());
        fields.put("keywords", String.join(", ", keywords));
        fields.put("description", page.getDescription());

        fields.put("topNavPages", getTopNavPages());
        fields.put("topVisits", getTopVisits(10));

        fields.put("googleAnalyticsId", siteConfig.getGoogleAnalyticsId());
        fields.put("googleAdsensePublisherId", siteConfig.getGoogleAdsensePublisherId());
        fields.put("googleAdsenseSearchId", siteConfig.getGoogleAdsenseSearchId());

        return fields;
    }

    SitePage getPage(String pageIdentifier) {
        String pageApiUrl = apiConfig.getUrlPage(pageIdentifier);
        return restTemplate.getForObject(pageApiUrl, SitePage.class);
    }

    @Cacheable(key = "\"top-nav-pages\"")
    public Collection<SitePage> getTopNavPages() {
        return getPages(apiConfig.getUrlTopNavPages());
    }

    @Cacheable(key = "\"top-visits\"")
    public Collection<Visit> getTopVisits(int topVisitCount) {
        ResponseEntity<Collection<Visit>> response = restTemplate.exchange(
                apiConfig.getUrlTopVisits(topVisitCount),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Visit>>(){});
        return response.getBody();
    }

    Collection<SitePage> getPages(String url) {
        ResponseEntity<Collection<SitePage>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<SitePage>>(){});
        return response.getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    HttpEntity<String> getRequestForPost(BaseModel modelObject) throws IOException {
        String jsonStr = modelObject.toJson();
        return new HttpEntity<>(jsonStr, getHeaders());
    }
}
