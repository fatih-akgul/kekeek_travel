package com.kekeek.travel.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageHierarchy extends BaseModel {
    private String parentIdentifier;
    private String childIdentifier;
    private Integer sequence;
    private Boolean primary;
}
