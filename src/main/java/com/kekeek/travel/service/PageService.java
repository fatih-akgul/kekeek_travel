package com.kekeek.travel.service;

import com.kekeek.travel.config.ApiConfig;
import com.kekeek.travel.config.SiteConfig;
import com.kekeek.travel.model.Content;
import com.kekeek.travel.model.SitePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames={"pages"})
public class PageService extends BaseService {

    @Autowired
    public PageService(RestTemplate restTemplate, SiteConfig siteConfig, ApiConfig apiConfig) {
        this.siteConfig = siteConfig;
        this.restTemplate = restTemplate;
        this.apiConfig = apiConfig;
    }

    @Cacheable(key = "\"homepage\"")
    public Map<String, Object> getHomePageAttributes() {
        String pageIdentifier = "homepage";
        Map<String, Object> pageData = getMetaFields(pageIdentifier);

        pageData.put("mainContent", getContent(pageIdentifier, "homepage"));
        pageData.put("centerLinks", getContent(pageIdentifier, "links-center"));
        pageData.put("faq", getContent(pageIdentifier, "faq"));
        pageData.put("latestArticles", getContent(pageIdentifier, "latest-articles"));

        return pageData;
    }

    @Cacheable(key = "\"site-map\"")
    public Map<String, Object> getSiteMapAttributes() {
        String pageIdentifier = "site-map";
        Map<String, Object> pageData = getMetaFields(pageIdentifier);

        ResponseEntity<List<SitePage>> response = restTemplate.exchange(
                apiConfig.getUrlAllArticles(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SitePage>>(){});
        List<SitePage> articles = response.getBody();

        pageData.put("articles", articles);

        return pageData;
    }

    @Cacheable(key = "#pageIdentifier")
    public Map<String, Object> getArticlePageAttributes(String pageIdentifier) {
        SitePage articlePage = getPage(pageIdentifier);
        Map<String, Object> pageData = getMetaFields(articlePage);

        pageData.put("article", articlePage);
        pageData.put("articlePage", articlePage);

        pageData.put("mainContent", getContent(pageIdentifier, pageIdentifier));
        pageData.put("articleImage", siteConfig.getUrlPageImage(pageIdentifier));

        String breadcrumbsUrl = apiConfig.getUrlPageBreadcrumbs(pageIdentifier);
        pageData.put("breadcrumbs", getPages(breadcrumbsUrl));

        String contentPageIdentifier = pageIdentifier;
        String parentUrl = apiConfig.getUrlPageParent(contentPageIdentifier);
        SitePage parent = restTemplate.getForObject(parentUrl, SitePage.class);
        if ("article-page".equals(articlePage.getContentType()) && parent != null) {
            VisitService.excludePageFromStats(articlePage);
            pageData.putAll(getMetaFields(articlePage, parent));
            pageData.put("article", parent);
            contentPageIdentifier = parent.getIdentifier();
            parentUrl = apiConfig.getUrlPageParent(contentPageIdentifier);
            parent = restTemplate.getForObject(parentUrl, SitePage.class);
        } else if (parent == null) {
            VisitService.excludePageFromStats(articlePage);
        } else {
            VisitService.addTitle(articlePage.getIdentifier(), articlePage.getDescription());
        }
        pageData.put("parent", parent);

        String childrenUrl = apiConfig.getUrlPageChildArticles(contentPageIdentifier);
        pageData.put("childArticles", getPages(childrenUrl));

        String pagesUrl = apiConfig.getUrlPageChildPages(contentPageIdentifier);
        Collection<SitePage> childPages = getPages(pagesUrl);
        pageData.put("childPages", childPages);

        return pageData;
    }

    private String getContent(String pageIdentifier, String contentIdentifier) {
        String contentUrl = apiConfig.getUrlContent(pageIdentifier, contentIdentifier);
        Content content = restTemplate.getForObject(contentUrl, Content.class);
        if (content != null) {
            return content.getContentText();
        }

        return null;
    }
}
