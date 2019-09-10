package com.kekeek.travel.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Content extends EntityModel {
    private String identifier;
    private String title;
    private String description;
    private String language = "en";
    private String snippet;
    private Integer sequence;
    private String contentText;
}
