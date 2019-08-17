package com.kekeek.travel.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.util.Date;

@Data
class KekeekModel {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String website;

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static <T> T fromJson(String json, Class<T> tClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, tClass);
    }
}
