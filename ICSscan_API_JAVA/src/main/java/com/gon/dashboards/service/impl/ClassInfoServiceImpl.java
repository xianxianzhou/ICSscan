package com.gon.dashboards.service.impl;

import com.gon.dashboards.entity.ClassInfo;
import com.gon.dashboards.service.ClassInfoService;
import com.gon.dashboards.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassInfoServiceImpl implements ClassInfoService {
    @Autowired
    private DBService dbService;

    @Override
    public void add(ClassInfo classInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("classId", classInfo.getClassId());
        map.put("chainId", classInfo.getChainId());
        List<ClassInfo> list = dbService.find(map, ClassInfo.class);
        if (list == null || list.size() == 0) {
            dbService.save(classInfo);
        }
    }
}
