package com.gon.dashboards.task;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gon.dashboards.configuration.NodeConfiguration;
import com.gon.dashboards.entity.BlockResult;
import com.gon.dashboards.entity.ChainInfo;
import com.gon.dashboards.entity.RpcResult;
import com.gon.dashboards.service.ChainInfoService;
import com.gon.dashboards.util.DataUtils;
import com.gon.dashboards.util.HttpClientUtil;
import com.gon.dashboards.util.RpcCollect;
import com.gon.dashboards.util.ThreadPoolTaskExecutorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * iris Task
 */


@Slf4j
@Component
public class irisTask extends DataAbstract {

    @Autowired
    private NodeConfiguration nodeConfiguration;

    @Autowired
    private ChainInfoService chainInfoService;

   @Scheduled(cron = "*/1 * * * * ?")
    public void getBlockInfo() {


        try {
            ChainInfo chainInfo = chainInfoService.getByChainid(nodeConfiguration.getIrisChainid());
            if (chainInfo == null) {
                return;
            }
            Long height = chainInfo.getHeight();
            String url = nodeConfiguration.getIrisApi() + String.format(getBlockByHeight, height);
            log.info("chain id: {} get block height: {}",getChainId(),height);
            String resultStr = HttpClientUtil.get(url);
            if (resultStr != null) {
                BlockResult blockResult = JSONObject.parseObject(resultStr, new TypeReference<BlockResult>() {
                });
                if (blockResult != null && blockResult.getCode() == 0) {

                    if (blockResult.getBlock().getData().getTxs() != null && blockResult.getBlock().getData().getTxs().size() > 0) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                List<String> list = blockResult.getBlock().getData().getTxs();
                                for (String tx : list) {
                                    try {
                                        String hash = DataUtils.codeToHash(tx);
                                        log.info("chain id: {},hash:{}",getChainId(),hash);
                                        String transactionUrl = nodeConfiguration.getIrisApi() + String.format(txs, hash);
                                        String transactionResult = HttpClientUtil.get(transactionUrl);
                                        transactionInfo(transactionResult);

                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        ThreadPoolTaskExecutorUtils.execute(runnable);

                    }


                    height++;
                    chainInfoService.updateHeight(nodeConfiguration.getIrisChainid(), height);

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public String getChainId() {
        return nodeConfiguration.getIrisChainid();
    }
}
