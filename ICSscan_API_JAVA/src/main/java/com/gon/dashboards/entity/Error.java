package com.gon.dashboards.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Error implements Serializable {

    private Integer code;
    private String message;
    private String data;

    public Error(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
