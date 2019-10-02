package com.kekeek.travel.service;

import com.kekeek.travel.config.ApiConfig;
import com.kekeek.travel.model.SitePage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

abstract class BaseService {

    ApiConfig apiConfig;
    RestTemplate restTemplate;

    void setMetaFields(SitePage page, Model model) {
        setMetaFields(page, model, null);
    }

    void setMetaFields(String pageIdentifier, Model model) {
        SitePage page = getPage(pageIdentifier);
        setMetaFields(page, model);
    }

    void setMetaFields(SitePage page, Model model, SitePage parentArticle) {
        Set<String> keywords = new HashSet<>(page.getKeywords());
        if (parentArticle != null) {
            keywords.addAll(parentArticle.getKeywords());
        }

        model.addAttribute("pageTitle", page.getDescription());
        model.addAttribute("keywords", String.join(", ", keywords));
        model.addAttribute("description", page.getDescription());

        addPagesToModel(model, apiConfig.getUrlTopNavPages(), "topNavPages");
    }

    SitePage getPage(String pageIdentifier) {
        String pageApiUrl = apiConfig.getUrlPage(pageIdentifier);
        return restTemplate.getForObject(pageApiUrl, SitePage.class);
    }

    void addPagesToModel(Model model, String url, String modelKey) {
        ResponseEntity<Collection<SitePage>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<SitePage>>(){});
        Collection<SitePage> pages = response.getBody();
        model.addAttribute(modelKey, pages);
    }
}
