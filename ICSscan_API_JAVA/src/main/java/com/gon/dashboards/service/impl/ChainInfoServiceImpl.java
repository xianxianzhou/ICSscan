package com.gon.dashboards.service.impl;

import com.gon.dashboards.entity.ChainInfo;
import com.gon.dashboards.service.ChainInfoService;
import com.gon.dashboards.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChainInfoServiceImpl implements ChainInfoService {

    @Autowired
    private DBService dbService;

    @Override
    public void loading(List<ChainInfo> chainInfoList) {

        List<ChainInfo> chainInfos = dbService.find(null, ChainInfo.class);

        Map<String, ChainInfo> map = new HashMap<>();
        for (ChainInfo chainInfo : chainInfos) {
            map.put(chainInfo.getChainId(), chainInfo);
        }

        List<ChainInfo> add = new ArrayList<>();
        for (int i = 0; i < chainInfoList.size(); i++) {
            ChainInfo chainInfo = chainInfoList.get(i);
            ChainInfo db = map.get(chainInfo.getChainId());
            // reboot block height
            if (db != null && chainInfo.getReboot()) {
                db.setHeight(chainInfo.getHeight());
            } else {
                db = new ChainInfo();
                db.setChainId(chainInfo.getChainId());
                db.setHeight(chainInfo.getHeight());
            }
            db.setApi(chainInfo.getApi());
            add.add(db);
            map.remove(chainInfo.getChainId());
        }

        dbService.saveAll(add, ChainInfo.class);
        // delete chain info
        if (map.size() > 0) {
            for (String key : map.keySet()) {
                ChainInfo del = map.get(key);
                dbService.delete(del.getId(), ChainInfo.class);
            }
        }

    }

    @Override
    public void updateHeight(String chainId, Long height) {
        ChainInfo chainInfo = getByChainid(chainId);
        if (chainInfo != null) {

            if (height > chainInfo.getHeight()) {
                Map<String, Object> map = new HashMap<>();
                map.put("height", height);
                dbService.update(chainInfo.getId(), map, ChainInfo.class);
            }
        }
    }

    @Override
    public List<ChainInfo> findAll() {
        List<ChainInfo> chainInfos = dbService.find(null, ChainInfo.class);
        return chainInfos;
    }

    @Override
    public ChainInfo getByChainid(String chainId) {
        Map<String, Object> map = new HashMap<>();
        map.put("chainId", chainId);
        List<ChainInfo> chainInfos = dbService.find(map, ChainInfo.class);
        return chainInfos.get(0);
    }


}
