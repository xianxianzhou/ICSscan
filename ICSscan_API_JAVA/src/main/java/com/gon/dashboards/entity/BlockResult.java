package com.gon.dashboards.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BlockResult implements Serializable {

    private Block_id block_id;
    private Block block;
    private Integer code=0;
}
