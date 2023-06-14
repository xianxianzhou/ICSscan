package com.gon.dashboards.service;

import com.gon.dashboards.entity.IBCClass;

public interface IBCClassService {


    void add(IBCClass ibcClass);

    IBCClass getPathByIbcClassId(String ibcClassId);
}
