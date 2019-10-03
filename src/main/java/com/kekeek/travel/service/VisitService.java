package com.kekeek.travel.service;

import com.kekeek.travel.config.ApiConfig;
import com.kekeek.travel.model.Visit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class VisitService extends BaseService {
    private static ConcurrentMap<String, Integer> COUNTER = new ConcurrentHashMap<>();

    private ApiConfig apiConfig;
    private RestTemplate restTemplate;

    public VisitService(RestTemplate restTemplate, ApiConfig apiConfig) {
        this.restTemplate = restTemplate;
        this.apiConfig = apiConfig;
    }

    public void addVisit(String pageIdentifier) {
        COUNTER.putIfAbsent(pageIdentifier, 0);
        COUNTER.computeIfPresent(pageIdentifier, (key, value) -> value + 1);
    }

    @Scheduled(fixedDelay = 60_000, initialDelay = 120_000)
    private void sendVisits() {
        if (!COUNTER.isEmpty()) {
            Map<String, Integer> copy = new HashMap<>(COUNTER);
            COUNTER.clear();

            for (String pageIdentifier: copy.keySet()) {
                Integer counter = copy.get(pageIdentifier);
                try {
                    Visit visit = new Visit();
                    visit.setIdentifier(pageIdentifier);
                    visit.setCounter(counter);
                    restTemplate.postForObject(apiConfig.getUrlVisits(), getRequestForPost(visit), Visit.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
