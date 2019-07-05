package com.kekeek.travel.controller;

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
        String homePageApiUrl = properties.getApi().getUrlSitePage(homePageIdentifier);
        SitePage homePage = restTemplate.getForObject(homePageApiUrl, SitePage.class);

        if (homePage != null) {
            model.addAttribute("pageTitle", homePage.getTitle());
            model.addAttribute("keywords", homePage.getKeywords());
            model.addAttribute("description", homePage.getTitle());
        }
        return "index";
    }
}
