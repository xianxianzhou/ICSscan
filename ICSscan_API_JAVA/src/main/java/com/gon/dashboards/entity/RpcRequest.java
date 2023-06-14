package com.gon.dashboards.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;


public class RpcRequest<T> implements Serializable {

    private String jsonrpc = "2.0";
    private String id = "service_client";
    private String method;
    private T params;


    public  enum Method{
        broadcast_tx_commit,
        broadcast_tx_async
    }

    public RpcRequest() {
    }

    public RpcRequest(T params) {
        this.params = params;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
