package com.kekeek.travel.controller;

import com.kekeek.travel.model.Content;
import com.kekeek.travel.model.SitePage;
import com.kekeek.travel.properties.KekeekProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

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
        SitePage homePage = getPage(homePageIdentifier);

        if (homePage != null) {
            processPageMetaFields(homePage, model);

            addContentToModel(homePageIdentifier, "homepage", "mainContent", model);
            addContentToModel(homePageIdentifier, "links-center", "centerLinks", model);
            addContentToModel(homePageIdentifier, "faq", "faq", model);
            addContentToModel(homePageIdentifier, "latest-articles", "latestArticles", model);
        }
        return "index";
    }

    private SitePage getPage(String pageIdentifier) {
        String pageApiUrl = properties.getApi().getUrlForPage(pageIdentifier);
        return restTemplate.getForObject(pageApiUrl, SitePage.class);
    }

    private void processPageMetaFields(SitePage page, Model model) {
        model.addAttribute("pageTitle", page.getTitle());
        model.addAttribute("keywords", String.join(", ", page.getKeywords()));
        model.addAttribute("description", page.getDescription());
    }

    @GetMapping({"/{pageIdentifier}"})
    public String getArticle(Model model, @PathVariable final String pageIdentifier) {
        SitePage articlePage = getPage(pageIdentifier);

        model.addAttribute("article", articlePage);
        model.addAttribute("articlePage", articlePage);
        processPageMetaFields(articlePage, model);

        addContentToModel(pageIdentifier, pageIdentifier, "mainContent", model);
        model.addAttribute("articleImage", properties.getBaseImageUrl() + "/pages/" + pageIdentifier + "/" + pageIdentifier + ".jpg");

        String breadcrumbsUrl = properties.getApi().getUrlForPage(pageIdentifier) + "/breadcrumbs";
        addPagesToModel(model, breadcrumbsUrl, "breadcrumbs");

        String contentPageIdentifier = pageIdentifier;
        String parentUrl = properties.getApi().getUrlForPage(contentPageIdentifier) + "/parent";
        SitePage parent = restTemplate.getForObject(parentUrl, SitePage.class);
        if ("article-page".equals(articlePage.getContentType()) && parent != null) {
            model.addAttribute("article", parent);
            contentPageIdentifier = parent.getIdentifier();
            parentUrl = properties.getApi().getUrlForPage(contentPageIdentifier) + "/parent";
            parent = restTemplate.getForObject(parentUrl, SitePage.class);
        }
        model.addAttribute("parent", parent);

        if (parent != null) {
            String siblingsUrl = properties.getApi().getUrlForPage(parent.getIdentifier()) + "/children";
            addPagesToModel(model, siblingsUrl, "siblings");
        }

        String childrenUrl = properties.getApi().getUrlForPage(contentPageIdentifier) + "/children";
        addPagesToModel(model, childrenUrl, "children");

        return "article";
    }

    private void addPagesToModel(Model model, String url, String modelKey) {
        ResponseEntity<Collection<SitePage>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<SitePage>>(){});
        Collection<SitePage> pages = response.getBody();
        model.addAttribute(modelKey, pages);
    }

    private void addContentToModel(String pageIdentifier, String contentIdentifier, String nameInModel, Model model) {
        String contentUrl = properties.getApi().getUrlForContent(pageIdentifier, contentIdentifier);
        Content content = restTemplate.getForObject(contentUrl, Content.class);
        if (content != null) {
            model.addAttribute(nameInModel, content.getContentText());
        }
    }
}
