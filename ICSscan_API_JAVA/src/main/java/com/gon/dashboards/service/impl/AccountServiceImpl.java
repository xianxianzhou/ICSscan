package com.gon.dashboards.service.impl;

import com.gon.dashboards.entity.Account;
import com.gon.dashboards.service.AccountService;
import com.gon.dashboards.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;


@Service
public class AccountServiceImpl implements AccountService {


    @Autowired
    private DBService dbService;

    @Override
    public List<Account> allAccount() {

        List<Account> accountList = dbService.find(null, Account.class);
        return accountList;
    }

    @Override
    public Long count() {
        return dbService.countDocuments(Account.class);
    }

    @Override
    public List<Account> queryByPage(String search, Integer page, Integer size) {
        Query query = new Query();
        if (!StringUtils.isEmpty(search)) {
            Pattern pattern = Pattern.compile("^.*" + search + ".*$", Pattern.CASE_INSENSITIVE);
            Criteria criteria = new Criteria().orOperator(Criteria.where("teamName").regex(search),
                    Criteria.where("irisAddress").is(search),
                    Criteria.where("uptickAddress").is(search),
                    Criteria.where("junoAddress").is(search),
                    Criteria.where("stargazeAddress").is(search),
                    Criteria.where("omniflixAddress").is(search),
                    Criteria.where("discordHandle").is(search),
                    Criteria.where("community").regex(search));
            query.addCriteria(criteria);

        }
        List<Account> list = dbService.customFindByPage(page, size, query, null, Account.class);
        return list;
    }

    @Override
    public Boolean isExist(String address) {
        Query query = new Query();
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("irisAddress").is(address),
                Criteria.where("uptickAddress").is(address),
                Criteria.where("junoAddress").is(address),
                Criteria.where("stargazeAddress").is(address),
                Criteria.where("omniflixAddress").is(address)
        );
        query.addCriteria(criteria);
        List<Account> list = dbService.customFind(query, Account.class);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }
}
