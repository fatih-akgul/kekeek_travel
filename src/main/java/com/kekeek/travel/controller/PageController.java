package com.kekeek.travel.controller;

import com.kekeek.travel.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

    private PageService pageService;

    @Autowired
    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping({"/"})
    public String getHomePage(Model model) {
        pageService.populateHomePageData(model);

        return "index";
    }

    @GetMapping({"/site-map"})
    public String getSiteMap(Model model) {
        pageService.populateSiteMapData(model);

        return "site-map";
    }

    @GetMapping({"/{pageIdentifier}"})
    public String getArticlePage(Model model, @PathVariable final String pageIdentifier) {
        pageService.populateArticlePageData(model, pageIdentifier);

        return "article";
    }
}
