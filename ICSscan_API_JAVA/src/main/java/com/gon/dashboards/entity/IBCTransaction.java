package com.gon.dashboards.entity;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "ibc_transaction")
@ToString
@Data
public class IBCTransaction implements Serializable {


    private String id;
    private String sender;
    private String receiver;
    private List<String> tokenID;
    private String sequence;

    private String sourceChainId;
    private String sourcePort;
    private String sourceChannel;
    private String sourceClassID;
    private String sourceClassPath;
    private String sourceHeight;
    private String sourceTxid;
    private Long sourceTime;


    private String destinationChainId;
    private String destinationPort;
    private String destinationChannel;
    private String destinationClassID;
    private String destinationClassPath;
    private String destinationHeight;
    private String destinationTxid;
    private Long destinationTime;
    private String data;
    private String baseClassId;
}
