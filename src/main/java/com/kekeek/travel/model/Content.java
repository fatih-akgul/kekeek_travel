package com.kekeek.travel.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class Content extends KekeekModel {
    private String identifier;
    private String title;
    private String description;
    private String language;
    private String snippet;
    private Integer pageNumber;
    private String contentText;
    private String image;
    private String imageDescription;
}
