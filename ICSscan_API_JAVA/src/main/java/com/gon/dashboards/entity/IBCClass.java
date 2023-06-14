package com.gon.dashboards.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ibc_class")
@ToString
@Data
public class IBCClass {


    private String id;
    private String chainId;
    private String sourcePort;
    private String sourceChannel;
    private String destinationPort;
    private String destinationchannel;
    private String classPath;
    private String ibcClassId;
}
