package com.gon.dashboards.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.gon.dashboards.entity.Account;
import com.gon.dashboards.entity.ChainInfo;
import com.gon.dashboards.service.ChainInfoService;
import com.gon.dashboards.service.DBService;
import com.gon.dashboards.util.ExcelUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Configuration
@ConfigurationProperties(prefix = "config")
@Setter
@Getter
@Slf4j
public class NodeConfiguration {


    private String uptickApi;
    private String uptickChainid;
    private Long uptickHeight;
    private Boolean uptickReboot = false;


    private String irisApi;
    private String irisChainid;
    private Long irisHeight;
    private Boolean irisReboot = false;


    private String stargazeApi;
    private String stargazeChainid;
    private Long stargazeHeight;
    private Boolean stargazeReboot = false;


    private String junoApi;
    private String junoChainid;
    private Long junoHeight;
    private Boolean junoReboot = false;


    private String omniflixApi;
    private String omniflixChainid;
    private Long omniflixHeight;
    private Boolean omniflixReboot = false;

    private String evidencePath;
    private Boolean updateAccount;

    @Autowired
    private DBService dbService;


    //update chain info
    @Bean
    public void updateDb() throws IOException {

        if (uptickChainid != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("chainId", uptickChainid);
            List<ChainInfo> upticks = dbService.find(map, ChainInfo.class);
            if (upticks != null && upticks.size() > 0) {
                ChainInfo uptick = upticks.get(0);
                map.put("api", uptickApi);
                if (uptickReboot) {
                    map.put("height", uptickHeight);
                }
                dbService.update(uptick.getId(), map, ChainInfo.class);

            } else {
                ChainInfo uptick = new ChainInfo();
                uptick.setApi(uptickApi);
                uptick.setHeight(uptickHeight);
                uptick.setChainId(uptickChainid);
                dbService.save(uptick);
            }
        }

        if (irisChainid != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("chainId", irisChainid);
            List<ChainInfo> iriss = dbService.find(map, ChainInfo.class);
            if (iriss != null && iriss.size() > 0) {
                ChainInfo iris = iriss.get(0);
                map.put("api", irisApi);
                if (irisReboot) {
                    map.put("height", irisHeight);
                }
                dbService.update(iris.getId(), map, ChainInfo.class);

            } else {
                ChainInfo iris = new ChainInfo();
                iris.setApi(irisApi);
                iris.setHeight(irisHeight);
                iris.setChainId(irisChainid);
                dbService.save(iris);
            }
        }
        if (stargazeChainid != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("chainId", stargazeChainid);
            List<ChainInfo> stargazes = dbService.find(map, ChainInfo.class);
            if (stargazes != null && stargazes.size() > 0) {
                ChainInfo stargaze = stargazes.get(0);
                map.put("api", stargazeApi);
                if (stargazeReboot) {
                    map.put("height", stargazeHeight);
                }
                dbService.update(stargaze.getId(), map, ChainInfo.class);

            } else {
                ChainInfo stargaze = new ChainInfo();
                stargaze.setApi(stargazeApi);
                stargaze.setHeight(stargazeHeight);
                stargaze.setChainId(stargazeChainid);
                dbService.save(stargaze);
            }
        }
        if (junoChainid != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("chainId", junoChainid);
            List<ChainInfo> junos = dbService.find(map, ChainInfo.class);
            if (junos != null && junos.size() > 0) {
                ChainInfo juno = junos.get(0);
                map.put("api", junoApi);
                if (junoReboot) {
                    map.put("height", junoHeight);
                }
                dbService.update(juno.getId(), map, ChainInfo.class);

            } else {
                ChainInfo juno = new ChainInfo();
                juno.setApi(junoApi);
                juno.setHeight(junoHeight);
                juno.setChainId(junoChainid);
                dbService.save(juno);
            }
        }
        if (omniflixChainid != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("chainId", omniflixChainid);
            List<ChainInfo> omniflixs = dbService.find(map, ChainInfo.class);
            if (omniflixs != null && omniflixs.size() > 0) {
                ChainInfo omniflix = omniflixs.get(0);
                map.put("api", omniflixApi);
                if (omniflixReboot) {
                    map.put("height", omniflixHeight);
                }
                dbService.update(omniflix.getId(), map, ChainInfo.class);

            } else {
                ChainInfo omniflix = new ChainInfo();
                omniflix.setApi(omniflixApi);
                omniflix.setHeight(omniflixHeight);
                omniflix.setChainId(omniflixChainid);
                dbService.save(omniflix);
            }
        }


    }


    //update account
    @Bean
    public void readAccount() {

        if (updateAccount==null||!updateAccount){
            return;
        }
        File file = new File(evidencePath );
        File[] listFiles = file.listFiles();
        List<Account> accounts = new ArrayList<>();
        for (File var : listFiles) {
            if (!var.isFile() && !var.getName().startsWith(".")) {
                try {
                    File[] files = var.listFiles();
                    for (int i = 0; i <files.length ; i++) {
                        File acount = files[i];
                        FileInputStream inputStream = new FileInputStream(acount);
                        List<Account> accountList = ExcelUtils.readAccountExcel(inputStream, acount.getPath());
                        if (accountList != null) {
                            accounts.addAll(accountList);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
        if (accounts.size() > 0) {
            dbService.deleteDocuments(Account.class);
            dbService.saveAll(accounts, Account.class);
        }


    }


//    public void updateDb() throws IOException {
//
//        String path = "/config.json";
//        InputStream inputStream = getClass().getResourceAsStream(path);
//        if(inputStream==null){
//            throw new RuntimeException("The config file does not exist！！！");
//        }else{
//            String json = JSON.parseObject(inputStream,String.class);
//            List<ChainInfo> chainInfoList= JSONArray.parseArray(json,ChainInfo.class);
//            List<ChainInfo> chainInfos = dbService.find(null, ChainInfo.class);
//
//            Map<String, ChainInfo> map = new HashMap<>();
//            for (ChainInfo chainInfo : chainInfos) {
//                map.put(chainInfo.getChainId(), chainInfo);
//            }
//
//            List<ChainInfo> add = new ArrayList<>();
//            for (int i = 0; i < chainInfoList.size(); i++) {
//                ChainInfo chainInfo = chainInfoList.get(i);
//                ChainInfo db = map.get(chainInfo.getChainId());
//                // reboot block height
//                if (db != null && chainInfo.getReboot()) {
//                    db.setHeight(chainInfo.getHeight());
//                } else {
//                    db = new ChainInfo();
//                    db.setChainId(chainInfo.getChainId());
//                    db.setHeight(chainInfo.getHeight());
//                }
//                db.setApi(chainInfo.getApi());
//                add.add(db);
//                map.remove(chainInfo.getChainId());
//            }
//
//            dbService.saveAll(add, ChainInfo.class);
//            // delete chain info
//            if (map.size() > 0) {
//                for (String key : map.keySet()) {
//                    ChainInfo del = map.get(key);
//                    dbService.delete(del.getId(), ChainInfo.class);
//                }
//            }
//
//        }
//
//    }


}
