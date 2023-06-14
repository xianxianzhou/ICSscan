package com.gon.dashboards.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T> {

    private String jsonrpc;
    private int id;
    private T result;

}
