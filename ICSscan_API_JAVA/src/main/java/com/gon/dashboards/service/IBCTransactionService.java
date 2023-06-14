package com.gon.dashboards.service;

import com.gon.dashboards.entity.IBCTransaction;

import java.util.List;

public interface IBCTransactionService {


    void add(IBCTransaction transaction);

    List<IBCTransaction> getCrossChainListByTxid(String txid);

    List<IBCTransaction> getCrossChainListByAddress(String address);

    Long count();

    List<IBCTransaction> queryByPage(String search, Integer page, Integer size);
}
