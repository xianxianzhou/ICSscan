package com.gon.dashboards.entity;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "class_info")
@ToString
@Data
public class ClassInfo {

    private String id;
    private String classId;
    private String name;
    private String symbol;
    private String schema;
    private String description;
    private String uri;
    private String uriHash;
    private String data;
    private String creater;
    private Long createTime;
    private String chainId;



}
