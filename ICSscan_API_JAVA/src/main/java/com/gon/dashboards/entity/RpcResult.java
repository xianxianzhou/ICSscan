package com.gon.dashboards.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RpcResult<T> implements Serializable {

    private String jsonrpc = "2.0";
    private String id = "service_client";
    private T result;
    private Error error;


    public RpcResult(String error) {
        this.error = new Error(-1, error);
    }

    public RpcResult() {

    }
}
