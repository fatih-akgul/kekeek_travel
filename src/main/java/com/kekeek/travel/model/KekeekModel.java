package com.kekeek.travel.model;

import lombok.Data;
import java.util.Date;

@Data
class KekeekModel {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String website;
}
