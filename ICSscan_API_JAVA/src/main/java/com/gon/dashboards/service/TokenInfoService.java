package com.gon.dashboards.service;

import com.gon.dashboards.entity.TokenInfo;

public interface TokenInfoService {

    void add(TokenInfo tokenInfo);

    TokenInfo queryOne(String chainid, String classId, String tokenId);

    void updateOwnerById(String id, String owner, Long updateTime);

    void burn(String chainid, String classId, String tokenId);
}
