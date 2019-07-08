package com.kekeek.travel.util;

import com.kekeek.travel.model.Content;
import com.kekeek.travel.model.SitePage;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.stream.Stream;

public class ContentLoader {

    private final static String TOP_DIR_NAME = "src/main/resources/content";
    private final static String API_URL = "http://localhost:3080/pages/";

    public static void main(String[] args) {
        var pageDirectories = new File(TOP_DIR_NAME).listFiles();

        if (pageDirectories == null) {
            System.out.println("There are no files or subdirectories under " + TOP_DIR_NAME);
            return;
        }

        Scanner reader = new Scanner(System.in);
        System.out.print("Enter the api username: ");
        String username = reader.nextLine();
        System.out.println();

        System.out.print("Enter the api password: ");
        String password = reader.nextLine();
        System.out.println();

        RestTemplate restTemplate = new RestTemplateBuilder().basicAuthentication(username, password).build();

        Stream.of(pageDirectories).filter(File::isDirectory).forEach(pageDir -> {
            String pageName = pageDir.getName();
            SitePage page = null;
            try {
                page = restTemplate.getForObject(API_URL + pageName, SitePage.class);
            } catch (Exception e) {
                System.out.println("Exception of type " + e.getClass() + " while getting page " + pageName);
            }

            if (page != null) {
                var files = pageDir.listFiles();
                if (files != null) {
                    Stream.of(files).forEach(contentFile -> {
                        String contentIdentifier = contentFile.getName().substring(0, contentFile.getName().lastIndexOf(".html"));
                        System.out.println(">>>>> " + pageName + "/" + contentIdentifier);


                        String contentOnDisk;

                        try {
                            contentOnDisk = Files.readString(contentFile.toPath());
                            try {
                                Content content = restTemplate.getForObject(API_URL + pageName, Content.class);
                                if (content != null && !contentOnDisk.equals(content.getContentText())) {
                                    // Update content if different
                                    content.setContentText(contentOnDisk);

                                    HttpEntity<Content> request = new HttpEntity<>(content);
                                    restTemplate.put(API_URL + pageName + "/contents/" + contentIdentifier, request);
                                }
                            } catch (Exception e) {
                                // Insert content
                                Content content = new Content();
                                content.setIdentifier(contentIdentifier);
                                content.setContentText(contentOnDisk);

                                HttpEntity<Content> request = new HttpEntity<>(content);
                                restTemplate.postForObject(API_URL + pageName + "/contents", request, Content.class);
                            }
                        } catch (IOException e) {
                            System.out.println("IOException of type " + e.getClass() + " while reading file " + contentFile.getAbsolutePath());
                        }
                    });
                }
            }
        });
    }
}
