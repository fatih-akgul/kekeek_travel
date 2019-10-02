package com.kekeek.travel.service;

import com.kekeek.travel.config.ApiConfig;
import com.kekeek.travel.config.EmailConfig;
import com.kekeek.travel.config.SiteConfig;
import com.kekeek.travel.model.Content;
import com.kekeek.travel.model.SitePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class PageService extends BaseService {
    private SiteConfig siteConfig;
    private EmailConfig emailConfig;
    private HttpServletRequest request;

    @Autowired
    public PageService(RestTemplate restTemplate, SiteConfig siteConfig, ApiConfig apiConfig) {
        this.siteConfig = siteConfig;
        this.restTemplate = restTemplate;
        this.apiConfig = apiConfig;
    }

    public void populateHomePageData(Model model) {
        String homePageIdentifier = "homepage";
        setMetaFields(homePageIdentifier, model);

        addContentToModel(homePageIdentifier, "homepage", "mainContent", model);
        addContentToModel(homePageIdentifier, "links-center", "centerLinks", model);
        addContentToModel(homePageIdentifier, "faq", "faq", model);
        addContentToModel(homePageIdentifier, "latest-articles", "latestArticles", model);
    }

    public void populateSiteMapData(Model model) {
        String siteMapIdentifier = "site-map";
        setMetaFields(siteMapIdentifier, model);

        ResponseEntity<List<SitePage>> response = restTemplate.exchange(
                apiConfig.getUrlAllArticles(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SitePage>>(){});
        List<SitePage> articles = response.getBody();
        model.addAttribute("articles", articles);
    }

    public void populateArticlePageData(Model model, String pageIdentifier) {
        SitePage articlePage = getPage(pageIdentifier);

        model.addAttribute("article", articlePage);
        model.addAttribute("articlePage", articlePage);
        setMetaFields(articlePage, model);

        addContentToModel(pageIdentifier, pageIdentifier, "mainContent", model);
        model.addAttribute("articleImage", siteConfig.getUrlPageImage(pageIdentifier));

        String breadcrumbsUrl = apiConfig.getUrlPageBreadcrumbs(pageIdentifier);
        addPagesToModel(model, breadcrumbsUrl, "breadcrumbs");

        String contentPageIdentifier = pageIdentifier;
        String parentUrl = apiConfig.getUrlPageParent(contentPageIdentifier);
        SitePage parent = restTemplate.getForObject(parentUrl, SitePage.class);
        if ("article-page".equals(articlePage.getContentType()) && parent != null) {
            setMetaFields(articlePage, model, parent);
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
    }

    private void addContentToModel(String pageIdentifier, String contentIdentifier, String nameInModel, Model model) {
        String contentUrl = apiConfig.getUrlContent(pageIdentifier, contentIdentifier);
        Content content = restTemplate.getForObject(contentUrl, Content.class);
        if (content != null) {
            model.addAttribute(nameInModel, content.getContentText());
        }
    }
}
