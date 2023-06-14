package com.gon.dashboards.entity;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "chain_info")
@ToString
@Data
public class ChainInfo implements Serializable {


    private String id;

    private String api;
    private String chainId;
    private Long height;
    private Boolean reboot = false;
}
