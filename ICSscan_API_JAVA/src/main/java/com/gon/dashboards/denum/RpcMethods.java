package com.gon.dashboards.denum;

public enum RpcMethods {


    AbciQuery("abci_query"),
    Subscribe("subscribe"),
    Health("health"),
    Block("block"),
    BlockResults("block_results"),
    Tx("tx"),
    TxSearch("tx_search");


    private String method;

    RpcMethods(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
