package com.gon.dashboards.entity;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;


@Getter
@Setter
public class JsonResult<T> implements Serializable {
    private Integer code = 0;
    private Boolean success;
    private String msg;
    private T data;
    private JSONObject map = new JSONObject();

    public JsonResult() {

    }

    public JsonResult(boolean success, Integer code, String message) {
        this(success, code, message, null);
    }

    public JsonResult(boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.msg = message;
        this.data = data;
    }


    public static <T> JsonResult<T> success() {
        return new JsonResult<>(true, 0, "SUCCESS");
    }



    public static <U> JsonResult<U> success(U data) {

        return new JsonResult<>(true, 0, "SUCCESS", data);
    }






    public static <U> JsonResult<U> error(U data) {
        return new JsonResult<>(false, -1,"ERROR", data);

    }



    public static <T> JsonResult<T> error(Integer code, String message) {
        return new JsonResult<>(false, code, message);
    }



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
