package com.gon.dashboards.controller;

import com.gon.dashboards.entity.IBCClass;
import com.gon.dashboards.entity.IBCTransaction;
import com.gon.dashboards.entity.JsonResult;
import com.gon.dashboards.service.IBCClassService;
import com.gon.dashboards.service.IBCTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "跨链记录Api")
public class CrossController {


    @Autowired
    private IBCTransactionService iscTransactionService;


    @Autowired
    private IBCClassService ibcClassService;


    @ApiOperation("根据跨链交易id,查询跨链记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "txid", value = "交易id", required = true, dataType = "String", paramType = "query")

    })
    @PostMapping("/getCrossChainListByTxid")
    public JsonResult<List<IBCTransaction>> getCrossChainListByTxid(@RequestParam String txid) {

        List<IBCTransaction> iscTransactionList = iscTransactionService.getCrossChainListByTxid(txid);
        return JsonResult.success(iscTransactionList);
    }

    @ApiOperation("根据地址,查询跨链记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "钱包地址", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/getCrossChainListByAddress")
    public JsonResult<List<IBCTransaction>> getCrossChainListByAddress(@RequestParam String address) {

        List<IBCTransaction> iscTransactionList = iscTransactionService.getCrossChainListByAddress(address);
        return JsonResult.success(iscTransactionList);
    }


    @ApiOperation("解析ibc class id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ibcClassId", value = "ibc class id", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/analyzeClass")
    public JsonResult<IBCClass> analyzeClass(@RequestParam String ibcClassId) {
        IBCClass ibcClass = ibcClassService.getPathByIbcClassId(ibcClassId);
        if (ibcClass != null) {
            // String path=ibcClass.getSourcePort()+"/"+ibcClass.getSourceChannel()+"/"+ibcClass.getClassId();
            return JsonResult.success(ibcClass);
        } else {
            return JsonResult.error(-1, "Ibc Class Id does not exist");
        }
    }
}
