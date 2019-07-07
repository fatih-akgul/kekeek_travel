package com.kekeek.travel.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class SitePage extends KekeekModel {
    private String identifier;
    private String title;
    private String description;
    private String language;
    private Collection<String> keywords;
}
