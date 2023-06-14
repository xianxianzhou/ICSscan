package com.gon.dashboards.entity;

import java.time.Instant;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class Header {

    private String chain_id;
    private String height;
    private String time;
    private Date dateTime;
    private Long timestamp;


    public void setTime(String time) {
        this.dateTime = java.util.Date.from(Instant.parse(time));
        this.time = time;
        this.timestamp = dateTime.getTime() / 1000;
    }
}
