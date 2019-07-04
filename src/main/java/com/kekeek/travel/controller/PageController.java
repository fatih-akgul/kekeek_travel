package com.kekeek.travel.controller;

import com.kekeek.travel.model.SitePage;
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

    @Autowired
    public PageController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping({"/"})
    public String handleRequest(Model model) {
        SitePage homePage = restTemplate.getForObject("http://localhost:3080/site_pages/homepage", SitePage.class);

        if (homePage != null) {
            model.addAttribute("pageTitle", homePage.getTitle());
        }
        return "index";
    }
}
