package com.kekeek.travel.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class SitePage extends KekeekModel {
    private String identifier;
    private String language = "en";
    private String title;
    private String description;
    private String snippet;
    private Integer pageCount;
    private String contentType;
    private String status;
    private Collection<String> keywords;

    private String parentPageId;
    private String image;
    private String imageDescription;

    private String parentPageIdentifier;
}
