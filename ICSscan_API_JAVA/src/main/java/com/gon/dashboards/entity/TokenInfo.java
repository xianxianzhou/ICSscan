package com.gon.dashboards.entity;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "token_info")
@ToString
@Data
public class TokenInfo {

    private String id;
    private String classId;
    private String baseClassId;
    private String tokenId;
    private String uri;
    private String uriHash;
    private String data;
    private String name;
    private String owner;
    private String chainId;
    private Long createTime;
    private Long updateTime;
}
