package com.kekeek.travel.controller;

import com.kekeek.travel.model.Content;
import com.kekeek.travel.model.SitePage;
import com.kekeek.travel.properties.KekeekProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
public class PageController {

    private RestTemplate restTemplate;
    private KekeekProperties properties;

    @Autowired
    public PageController(RestTemplate restTemplate, KekeekProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @GetMapping({"/"})
    public String getHomePage(Model model) {
        String homePageIdentifier = "homepage";
        String homePageApiUrl = properties.getApi().getUrlForPage(homePageIdentifier);
        SitePage homePage = restTemplate.getForObject(homePageApiUrl, SitePage.class);

        if (homePage != null) {
            model.addAttribute("pageTitle", homePage.getTitle());
            model.addAttribute("keywords", String.join(", ", homePage.getKeywords()));
            model.addAttribute("description", homePage.getDescription());

            addContentToModel(homePageIdentifier, "main-content", "mainContent", model);
            addContentToModel(homePageIdentifier, "links-center", "centerLinks", model);
            addContentToModel(homePageIdentifier, "faq", "faq", model);
            addContentToModel(homePageIdentifier, "latest-articles", "latestArticles", model);
        }
        return "index";
    }

    private void addContentToModel(String pageIdentifier, String contentIdentifier, String nameInModel, Model model) {
        String contentUrl = properties.getApi().getUrlForContent(pageIdentifier, contentIdentifier);
        Content content = restTemplate.getForObject(contentUrl, Content.class);
        if (content != null) {
            model.addAttribute(nameInModel, content.getContentText());
        }
    }
}
