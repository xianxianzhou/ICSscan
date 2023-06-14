package com.gon.dashboards.service;

import com.gon.dashboards.entity.TokenInfo;

import java.util.List;
import java.util.Map;

public interface TokenIdInfoService {



     List<TokenInfo> queryTokenIdInfo(Map<String,Object> search, Integer page, Integer size);

    Long count();

    List<TokenInfo> queryByPage(String search, Integer page, Integer size);
}
