package com.gon.dashboards.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gon.dashboards.denum.RpcMethods;
import com.gon.dashboards.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


import java.util.Base64;
import java.util.List;


@Slf4j
public class RpcCollect {

    private String rpc;

    public static RpcCollect build(String rpc) {
        RpcCollect rpcUtil = new RpcCollect(rpc);
        return rpcUtil;
    }


    private RpcCollect(String rpc) {
        this.rpc = rpc;
    }

    public RpcResult<String> searchTxs(String rpcUrl, Integer page, Integer size, String orderBy, List<Condition> conditions) {
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "desc";
        }
        EventQueryBuilder eventQueryBuilder = new EventQueryBuilder();
        conditions.forEach(condition -> {
            eventQueryBuilder.addCondition(condition);
        });

        String query = eventQueryBuilder.build();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", query);
        jsonObject.put("order_by", orderBy);
        if (page != null) {
            jsonObject.put("page", page.toString());
        }
        if (size != null) {
            jsonObject.put("per_page", size.toString());
        }
        RpcResult<String> result = request(RpcMethods.TxSearch, jsonObject, new TypeReference<RpcResult<String>>() {
        });
        return result;

    }


    public RpcResult<String> queryTxByHash(String hash) {

        JSONObject jsonObject = new JSONObject();

        hash = Base64.getEncoder().encodeToString(DataUtils.hex2byte(hash));

        jsonObject.put("hash", hash);

        RpcResult<String> result = request(RpcMethods.Tx, jsonObject, new TypeReference<RpcResult<String>>() {
        });

        return result;

    }


    public RpcResult<BlockResult> queryBlock(String height) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("height", height);
        RpcResult<BlockResult> rpcResult = request(RpcMethods.Block, jsonObject, new TypeReference<RpcResult<BlockResult>>() {
        });
        return rpcResult;

    }


    private <T> T request(RpcMethods rpcMethods, Object data, TypeReference<T> type) {
        try {
            RpcRequest msg = new RpcRequest(data);
            msg.setMethod(rpcMethods.getMethod());
            String res = HttpClientUtil.post(rpc, JSON.toJSONString(msg));
            RpcResult<T> rpcResult = new RpcResult<T>();
            rpcResult = (RpcResult<T>) JSONObject.parseObject(res, type);


            return (T) rpcResult;
        } catch (Exception e) {
            log.error("{}", e);
            return (T) new RpcResult(e.getMessage());
        }


    }

}
