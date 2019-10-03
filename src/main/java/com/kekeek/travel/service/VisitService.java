package com.kekeek.travel.service;

import com.kekeek.travel.config.ApiConfig;
import com.kekeek.travel.model.SitePage;
import com.kekeek.travel.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class VisitService extends BaseService {
    private static Map<String, Integer> VISIT_COUNTER = new ConcurrentHashMap<>();
    private static Collection<String> IGNORE_VISITS = new ConcurrentSkipListSet<>();
    private static Map<String, String> IDENTIFIER_TO_TITLE = new ConcurrentHashMap<>();

    private ApiConfig apiConfig;
    private RestTemplate restTemplate;

    @Autowired
    public VisitService(RestTemplate restTemplate, ApiConfig apiConfig) {
        this.restTemplate = restTemplate;
        this.apiConfig = apiConfig;
    }

    public void addVisit(String pageIdentifier) {
        if (shouldRecordVisits(pageIdentifier)) {
            VISIT_COUNTER.putIfAbsent(pageIdentifier, 0);
            VISIT_COUNTER.computeIfPresent(pageIdentifier, (key, value) -> value + 1);
        }
    }

    public static void addTitle(String identifier, String title) {
        IDENTIFIER_TO_TITLE.putIfAbsent(identifier, title);
    }

    private String getTitle(String identifier) {
        return IDENTIFIER_TO_TITLE.getOrDefault(identifier, identifier);
    }

    static void excludePageFromStats(SitePage page) {
        IGNORE_VISITS.add(page.getIdentifier());
    }

    private boolean shouldRecordVisits(String pageIdentifier) {
        return !IGNORE_VISITS.contains(pageIdentifier);
    }

    @Scheduled(fixedDelay = 300_000)
    void sendVisits() {
        if (!VISIT_COUNTER.isEmpty()) {
            Map<String, Integer> copy = new HashMap<>(VISIT_COUNTER);
            VISIT_COUNTER.clear();

            for (String pageIdentifier: copy.keySet()) {
                Integer counter = copy.get(pageIdentifier);
                try {
                    Visit visit = new Visit();
                    visit.setIdentifier(pageIdentifier);
                    visit.setCounter(counter);
                    visit.setTitle(getTitle(pageIdentifier));
                    restTemplate.postForObject(apiConfig.getUrlVisits(), getRequestForPost(visit), Visit.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
