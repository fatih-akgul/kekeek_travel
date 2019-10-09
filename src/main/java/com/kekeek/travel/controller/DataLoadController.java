package com.kekeek.travel.controller;

import com.kekeek.travel.service.DataLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
public class DataLoadController {

    private DataLoadService dataLoadService;

    @Autowired
    public DataLoadController(DataLoadService dataLoadService) {
        this.dataLoadService = dataLoadService;
    }

    @PostMapping({"/loadContent"})
    public Map<String, String> loadContent(@RequestParam String directory) {
        return dataLoadService.loadContent(directory);
    }
}
