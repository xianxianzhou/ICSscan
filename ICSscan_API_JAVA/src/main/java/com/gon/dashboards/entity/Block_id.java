package com.gon.dashboards.entity;

import com.gon.dashboards.util.DataUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Base64;

@Getter
@Setter
public class Block_id implements Serializable {

    private String hash;

//    public void setHash(String hash) {
//        this.hash = DataUtils.byte2Hex(Base64.getDecoder().decode(hash)).toUpperCase();
//    }
}
