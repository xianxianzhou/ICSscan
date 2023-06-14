package com.gon.dashboards.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Condition implements Serializable {

    private String key;
    private String value;
    private String op;

    public Condition(String key) {
        this.key = key;
    }


    public Condition lte(String value) {
        return this.fill(value, "<=");
    }

    public Condition gte(String value) {
        return this.fill(value, ">=");
    }

    public Condition le(String value) {
        return this.fill(value, "<");
    }

    public Condition ge(String value) {
        return this.fill(value, ">");
    }

    public Condition eq(String value) {
        return this.fill(value, "=");
    }

    public Condition contains(String value) {
        return this.fill(value, "CONTAINS");
    }

    public String toString() {
        return this.key + this.op + "'" + this.value + "'";
    }

    private Condition fill(String value, String op) {
        this.value = value;
        this.op = op;
        return this;
    }
}
