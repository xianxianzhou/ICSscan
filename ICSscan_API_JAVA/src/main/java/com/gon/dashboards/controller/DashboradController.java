package com.gon.dashboards.controller;


import com.gon.dashboards.entity.*;
import com.gon.dashboards.service.AccountService;
import com.gon.dashboards.service.IBCTransactionService;
import com.gon.dashboards.service.TokenIdInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "Dashborad")
@RequestMapping("/dashborad")
public class DashboradController {


    @Autowired
    private AccountService accountService;


    @Autowired
    private TokenIdInfoService tokenIdInfoService;


    @Autowired
    private IBCTransactionService ibcTransactionService;

    @ApiOperation("首页")
    @PostMapping("/home")
    public JsonResult analyzeClass() {

        Long accountCount = accountService.count();
        Long tokenIdInfoCount = tokenIdInfoService.count();
        Long ibcTransactionCount = ibcTransactionService.count();
        Map<String, Long> count = new HashMap<>();
        count.put("accounts", accountCount);
        count.put("tokenIds", tokenIdInfoCount);
        count.put("transactions", ibcTransactionCount);
        return JsonResult.success(count);
    }


    @ApiOperation("用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "关键词", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "String", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "String", paramType = "query", defaultValue = "10")
    })
    @PostMapping("/accountList")
    public JsonResult<List<Account>> accountList(String search, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {

        List<Account> accountList = accountService.queryByPage(search, page, size);

        return JsonResult.success(accountList);
    }


    @ApiOperation("nft列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "关键词", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "String", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "String", paramType = "query", defaultValue = "10")
    })
    @PostMapping("/nftList")
    public JsonResult<List<TokenInfo>> nftList(String search, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {

        List<TokenInfo> tokenIdInfoList = tokenIdInfoService.queryByPage(search, page, size);

        return JsonResult.success(tokenIdInfoList);
    }


    @ApiOperation("IBC 跨链信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "关键词", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "String", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "String", paramType = "query", defaultValue = "10")
    })
    @PostMapping("/ibcTransactionList")
    public JsonResult<List<IBCTransaction>> ibcTransactionList(String search, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {

        List<IBCTransaction> tokenIdInfoList = ibcTransactionService.queryByPage(search, page, size);

        return JsonResult.success(tokenIdInfoList);
    }
}
