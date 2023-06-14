package com.gon.dashboards.service;

import com.gon.dashboards.entity.Account;

import java.util.List;

public interface AccountService {


    List<Account> allAccount();

    Long count();

    List<Account> queryByPage(String search,Integer page,Integer size);

    Boolean isExist(String address);
}
