package com.gon.dashboards.service;

import com.gon.dashboards.entity.ChainInfo;

import java.util.List;

public interface ChainInfoService {

    void loading(List<ChainInfo> chainInfoList);

    void updateHeight(String chainId,Long heighe);

    List<ChainInfo> findAll();

    ChainInfo getByChainid(String chainId);
}
