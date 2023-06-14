package com.gon.dashboards.service.impl;


import com.gon.dashboards.entity.TokenInfo;
import com.gon.dashboards.service.DBService;
import com.gon.dashboards.service.TokenInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TokenInfoServiceImpl implements TokenInfoService {


    @Autowired
    private DBService dbService;

    @Override
    public void add(TokenInfo tokenInfo) {
        TokenInfo token = queryOne(tokenInfo.getChainId(), tokenInfo.getClassId(), tokenInfo.getTokenId());
        if (token == null) {
            dbService.save(tokenInfo);
        }
    }


    @Override
    public TokenInfo queryOne(String chainid, String classId, String tokenId) {
        Map<String, Object> map = new HashMap<>();
        map.put("classId", chainid);
        map.put("chainId", classId);
        map.put("tokenId", tokenId);
        List<TokenInfo> list = dbService.find(map, TokenInfo.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateOwnerById(String id, String owner, Long updateTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("updateTime", updateTime);
        dbService.update(id, map, TokenInfo.class);
    }

    @Override
    public void burn(String chainid, String classId, String tokenId) {
        TokenInfo tokenInfo = queryOne(chainid, classId, tokenId);
        if (tokenInfo != null) {
            dbService.delete(tokenInfo.getId(), TokenInfo.class);
        }
    }
}
