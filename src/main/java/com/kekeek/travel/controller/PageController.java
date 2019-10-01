package com.kekeek.travel.controller;

import com.kekeek.travel.config.ApiConfig;
import com.kekeek.travel.config.SiteConfig;
import com.kekeek.travel.model.Content;
import com.kekeek.travel.model.SitePage;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class PageController {

    private RestTemplate restTemplate;
    private ApiConfig apiConfig;
    private SiteConfig siteConfig;

    @Autowired
    public PageController(RestTemplate restTemplate, ApiConfig apiConfig, SiteConfig siteConfig) {
        this.restTemplate = restTemplate;
        this.apiConfig = apiConfig;
        this.siteConfig = siteConfig;
    }

    @GetMapping({"/"})
    public String getHomePage(Model model) {
        String homePageIdentifier = "homepage";
        SitePage homePage = getPage(homePageIdentifier);

        if (homePage != null) {
            processPageMetaFields(homePage, model, null);

            addContentToModel(homePageIdentifier, "homepage", "mainContent", model);
            addContentToModel(homePageIdentifier, "links-center", "centerLinks", model);
            addContentToModel(homePageIdentifier, "faq", "faq", model);
            addContentToModel(homePageIdentifier, "latest-articles", "latestArticles", model);
        }
        return "index";
    }

    @GetMapping({"/site-map"})
    public String getSiteMap(Model model) {
        ResponseEntity<List<SitePage>> response = restTemplate.exchange(
                apiConfig.getUrlAllArticles(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SitePage>>(){});
        List<SitePage> articles = response.getBody();
        model.addAttribute("articles", articles);

        String siteName = siteConfig.getSiteName();
        String country = siteConfig.getCountry();
        model.addAttribute("siteName", siteName);
        model.addAttribute("pageTitle", "Site Map - " + siteName);
        model.addAttribute("keywords", country + ",site,map");
        model.addAttribute("description", "All articles on " + siteName + " website");

        return "site-map";
    }

    private SitePage getPage(String pageIdentifier) {
        String pageApiUrl = apiConfig.getUrlPage(pageIdentifier);
        return restTemplate.getForObject(pageApiUrl, SitePage.class);
    }

    private void processPageMetaFields(SitePage page, Model model, SitePage parentArticle) {
        Set<String> keywords = new HashSet<>(page.getKeywords());
        if (parentArticle != null) {
            keywords.addAll(parentArticle.getKeywords());
        }

        model.addAttribute("pageTitle", page.getDescription());
        model.addAttribute("keywords", String.join(", ", keywords));
        model.addAttribute("description", page.getDescription());
    }

    @GetMapping({"/{pageIdentifier}"})
    public String getArticle(Model model, @PathVariable final String pageIdentifier) {
        SitePage articlePage = getPage(pageIdentifier);

        model.addAttribute("article", articlePage);
        model.addAttribute("articlePage", articlePage);
        processPageMetaFields(articlePage, model, null);

        addContentToModel(pageIdentifier, pageIdentifier, "mainContent", model);
        model.addAttribute("articleImage", siteConfig.getUrlPageImage(pageIdentifier));

        String breadcrumbsUrl = apiConfig.getUrlPageBreadcrumbs(pageIdentifier);
        addPagesToModel(model, breadcrumbsUrl, "breadcrumbs");

        String contentPageIdentifier = pageIdentifier;
        String parentUrl = apiConfig.getUrlPageParent(contentPageIdentifier);
        SitePage parent = restTemplate.getForObject(parentUrl, SitePage.class);
        if ("article-page".equals(articlePage.getContentType()) && parent != null) {
            processPageMetaFields(articlePage, model, parent);
            model.addAttribute("article", parent);
            contentPageIdentifier = parent.getIdentifier();
            parentUrl = apiConfig.getUrlPageParent(contentPageIdentifier);
            parent = restTemplate.getForObject(parentUrl, SitePage.class);
        }
        model.addAttribute("parent", parent);

        String childrenUrl = apiConfig.getUrlPageChildArticles(contentPageIdentifier);
        addPagesToModel(model, childrenUrl, "childArticles");

        String pagesUrl = apiConfig.getUrlPageChildPages(contentPageIdentifier);
        addPagesToModel(model, pagesUrl, "childPages");

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
        String contentUrl = apiConfig.getUrlContent(pageIdentifier, contentIdentifier);
        Content content = restTemplate.getForObject(contentUrl, Content.class);
        if (content != null) {
            model.addAttribute(nameInModel, content.getContentText());
        }
    }
}
