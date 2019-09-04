package com.kekeek.travel.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
abstract class EntityModel extends BaseModel {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String website;
}
