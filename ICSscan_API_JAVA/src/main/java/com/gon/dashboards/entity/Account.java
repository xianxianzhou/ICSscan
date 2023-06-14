package com.gon.dashboards.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "account")
@ToString
@Data
public class Account {

    private String id;
    private String teamName;
    private String irisAddress;
    private String uptickAddress;
    private String junoAddress;
    private String stargazeAddress;
    private String omniflixAddress;
    private String discordHandle;
    private String community;


}
