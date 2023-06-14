package com.gon.dashboards.service.impl;

import com.gon.dashboards.entity.IBCClass;
import com.gon.dashboards.service.DBService;
import com.gon.dashboards.service.IBCClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class IBCClassServiceImpl implements IBCClassService {

    @Autowired
    private DBService dbService;

    @Override
    public void add(IBCClass ibcClass) {
        Map<String, Object> map = new HashMap<>();
        map.put("ibcClassId", ibcClass.getIbcClassId());
        List<IBCClass> ibcClassList = dbService.find(map, IBCClass.class);
        if (ibcClassList == null || ibcClassList.size() == 0) {
            dbService.save(ibcClass);
        }
    }

    @Override
    public IBCClass getPathByIbcClassId(String ibcClassId) {
        Map<String, Object> map = new HashMap<>();
        map.put("ibcClassId", ibcClassId);
        List<IBCClass> ibcClassList = dbService.find(map, IBCClass.class);
        if (ibcClassList != null && ibcClassList.size() > 0) {
            return ibcClassList.get(0);
        }
        return null;
    }
}
