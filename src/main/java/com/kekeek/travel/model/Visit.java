package com.kekeek.travel.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Visit extends EntityModel {
    private String identifier;
    private Integer counter;
}
