package com.gon.dashboards.service.impl;

import com.gon.dashboards.entity.TokenInfo;
import com.gon.dashboards.service.DBService;
import com.gon.dashboards.service.TokenIdInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class TokenIdInfoServiceImpl implements TokenIdInfoService {


    @Autowired
    private DBService dbService;


    @Override
    public List<TokenInfo> queryTokenIdInfo(Map<String, Object> search, Integer page, Integer size) {

        List<TokenInfo> list = dbService.findByPage(page, size, search, Sort.by(Sort.Order.desc("createTime")), TokenInfo.class);
        return list;
    }

    @Override
    public Long count() {
        return dbService.countDocuments(TokenInfo.class);
    }


    @Override
    public List<TokenInfo> queryByPage(String search, Integer page, Integer size) {
        Query query = new Query();
        if (!StringUtils.isEmpty(search)) {
            Pattern pattern = Pattern.compile("^.*" + search + ".*$", Pattern.CASE_INSENSITIVE);
            Criteria criteria = new Criteria().orOperator(Criteria.where("classId").is(pattern),
                    Criteria.where("tokenId").is(pattern),
                    Criteria.where("name").is(pattern),
                    Criteria.where("owner").is(pattern),
                    Criteria.where("chainId").is(pattern));
            query.addCriteria(criteria);
        }
        List<TokenInfo> list = dbService.customFindByPage(page, size, query, null, TokenInfo.class);
        return list;
    }

}
