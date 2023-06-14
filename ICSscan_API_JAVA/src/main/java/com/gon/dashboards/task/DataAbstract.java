package com.gon.dashboards.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gon.dashboards.entity.*;
import com.gon.dashboards.service.*;
import com.gon.dashboards.util.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
public abstract class DataAbstract {

    protected static String getBlockByHeight = "/cosmos/base/tendermint/v1beta1/blocks/%s";
    protected static String txs = "/cosmos/tx/v1beta1/txs/%S";


    @Autowired
    private IBCTransactionService iscTransactionService;

    @Autowired
    private IBCClassService ibcClassService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClassInfoService classInfoService;

    @Autowired
    private TokenInfoService tokenInfoService;


    public void transactionInfo(String transactionResult) {
        if (transactionResult != null) {
            transactionResult = transactionResult.replace("@", "/");
            JSONObject txs = JSONObject.parseObject(transactionResult);
            JSONObject tx_response = txs.getJSONObject("tx_response");

//           JSONObject tx_response = JSONObject.parseObject(transactionResult);
            if (tx_response == null) {
                return;
            }
            log.info("TransactionResult chain id: {},hash:{}",getChainId(),tx_response.getString("txhash"));
            Integer code = tx_response.getInteger("code");
            if (code != 0) {
                return;
            }
            String height = tx_response.getString("height");
            String txId = tx_response.getString("txhash");
            String time = tx_response.getString("timestamp");
            Long timestamp = java.util.Date.from(Instant.parse(time)).getTime();

            JSONObject tx = tx_response.getJSONObject("tx");
            JSONArray logs = tx_response.getJSONArray("logs");
            JSONArray messages = tx.getJSONObject("body").getJSONArray("messages");
            for (int i = 0; i < messages.size(); i++) {
                JSONObject message = messages.getJSONObject(i);
                String type = message.getString("/type");

                if (type.equals("/ibc.core.channel.v1.MsgRecvPacket")) { //cosmos and wasm nft IBC receiver
                    nftIBCReceiver(message, logs.getJSONObject(i), txId, height, timestamp);
                } else if (type.equals("/ibc.applications.nft_transfer.v1.MsgTransfer")) { // cosmos nft IBC send
                    nftIBCSend(message, logs.getJSONObject(i), txId, height, timestamp);
                } else if (type.equals("/cosmwasm.wasm.v1.MsgExecuteContract") && message.getJSONObject("msg") != null && message.getJSONObject("msg").getJSONObject("send_nft") != null) {  //swam nft IBC send

                    swamIBCSend(message, logs.getJSONObject(i), txId, height, timestamp);
                }
                // todo nft info
//                else if (type.equals("/OmniFlix.onft.v1beta1.MsgCreateDenom") || type.equals("/uptick.collection.v1.MsgIssueDenom") || type.equals("/irismod.nft.MsgIssueDenom")) {
//                    createDenom(message, timestamp);
//                } else if (type.equals("/uptick.collection.v1.MsgMintNFT") || type.equals("/irismod.nft.MsgMintNFT")) {
//                    mintNFT(message, timestamp);
//                } else if (type.equals("/OmniFlix.onft.v1beta1.MsgMintONFT")) {
//                    mintONFT(message, timestamp);
//                } else if (type.equals("/OmniFlix.onft.v1beta1.MsgTransferONFT") || type.equals("/irismod.nft.MsgTransferNFT") || type.equals("/uptick.collection.v1.MsgTransferNFT")) {
//                    transferNFT(message, timestamp);
//                }


            }
        }


    }

    /**
     * ibc  receive
     *
     * @param message
     * @param log
     * @param txId
     * @param height
     * @param timestamp
     */

    public void nftIBCReceiver(JSONObject message, JSONObject log, String txId, String height, Long timestamp) {

        //get channel info
        String sequence = message.getJSONObject("packet").getString("sequence");
        String sourcePort = message.getJSONObject("packet").getString("source_port");
        String sourceChannel = message.getJSONObject("packet").getString("source_channel");
        String destinationPort = message.getJSONObject("packet").getString("destination_port");
        String destinationchannel = message.getJSONObject("packet").getString("destination_channel");
        String dataCode = message.getJSONObject("packet").getString("data");
        String data = new String(Base64.getDecoder().decode(dataCode));

        //is nft cross
        JSONObject dataJson = JSONObject.parseObject(data);
        String sourceClassPath = dataJson.getString("classId");
        if (sourceClassPath == null) {
            return;
        }
        // nft base class id
        String[] strings = sourceClassPath.split("/");
        String baseClass = strings[strings.length - 1];


        //send info
        String receiver = dataJson.getString("receiver");
        String sender = dataJson.getString("sender");
        List<String> tokenIds = dataJson.getJSONArray("tokenIds").toJavaList(String.class);
        //List<String> tokenDatas = dataJson.getJSONArray("tokenData").toJavaList(String.class);

        //Parse event
        Map<String, JSONArray> eventsMap = eventToMap(log.getJSONArray("events"));


        //cosmos mint ibc
        JSONArray eventMint = eventsMap.get("cosmos.nft.v1beta1.EventMint");
        //wasm mint ibc
        JSONArray instantiate = eventsMap.get("instantiate");
        JSONArray execute = eventsMap.get("execute");


        String destinationClassID = "";
        String destinationClassPath = "";

        if (eventMint != null && eventMint.size() > 0) { //cosmos Mint token
            Map<String, String> eventMintMap = attributesToMap(eventMint);
            destinationClassID = eventMintMap.get("class_id").replace("\"", "");

            destinationClassPath = destinationPort + "/" + destinationchannel + "/" + sourceClassPath;
            ibcClassInfo(sourceChannel, sourcePort, destinationchannel, destinationPort, destinationClassPath, destinationClassID);

        } else if (instantiate != null && execute != null) { //Mint swam contract
            destinationClassID = execute.getJSONObject(execute.size() - 1).getString("value");
            destinationClassPath = destinationPort + "/" + destinationchannel + "/" + sourceClassPath;
            ibcClassInfo(sourceChannel, sourcePort, destinationchannel, destinationPort, destinationClassPath, destinationClassID);

        } else if (instantiate == null && execute != null) { //swam contract return
            destinationClassID = execute.getJSONObject(execute.size() - 1).getString("value");

            String sourcehead = sourcePort + "/" + sourceChannel + "/";
            destinationClassPath = sourceClassPath.replaceFirst(sourcehead, "");
        } else { //cosmos return

            // Exclude duplicate transactions of ibc
            JSONArray tokenPacket = eventsMap.get("non_fungible_token_packet");
            if (tokenPacket == null) {
                return;
            }

            String sourcehead = sourcePort + "/" + sourceChannel + "/";
            destinationClassPath = sourceClassPath.replaceFirst(sourcehead, "");

            if (destinationClassPath.contains("/channel-")) {
                try {
                    destinationClassID = "ibc/" + DataUtils.String2SHA256StrJava(destinationClassID.getBytes(StandardCharsets.UTF_8));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            } else {//Back to the origin
                destinationClassID = destinationClassPath;
            }
        }

        IBCTransaction transaction = new IBCTransaction();
        transaction.setDestinationTxid(txId);
        transaction.setDestinationHeight(height);
        transaction.setDestinationTime(timestamp);
        transaction.setDestinationChannel(destinationchannel);
        transaction.setDestinationPort(destinationPort);
        transaction.setDestinationChainId(getChainId());
        transaction.setDestinationClassPath(destinationClassPath);
        transaction.setDestinationClassID(destinationClassID);
        transaction.setReceiver(receiver);
        transaction.setSender(sender);
        transaction.setTokenID(tokenIds);
        transaction.setData(data);
        transaction.setSequence(sequence);
        transaction.setSourceChannel(sourceChannel);
        transaction.setSourcePort(sourcePort);
        transaction.setBaseClassId(baseClass);

        iscTransactionService.add(transaction);

    }


    public void ibcClassInfo(String sourceChannel, String sourcePort, String destinationchannel, String destinationPort, String classPath, String ibcClassId) {
        //sava ibc class
        IBCClass ibcClass = new IBCClass();
        ibcClass.setChainId(getChainId());
        ibcClass.setSourceChannel(sourceChannel);
        ibcClass.setSourcePort(sourcePort);
        ibcClass.setDestinationchannel(destinationchannel);
        ibcClass.setDestinationPort(destinationPort);
        ibcClass.setIbcClassId(ibcClassId);
        ibcClass.setClassPath(classPath);
        ibcClassService.add(ibcClass);
    }

    //todo mint token
//    public void mintNFT(String sourceChannel, String sourcePort,
//                        String destinationchannel, String destinationPort, String destinationClassID,
//                        String classId,
//                        List<String> tokenIds, List<String> tokenDatas, String receiver, String baseClass, Long timestamp) {
//        for (int i = 0; i < tokenIds.size(); i++) {
//            String tokenData = new String(Base64.getDecoder().decode(tokenDatas.get(i)));
//
//            TokenInfo tokenInfo = new TokenInfo();
//            tokenInfo.setTokenId(tokenIds.get(i));
//            tokenInfo.setData(tokenData);
//            tokenInfo.setOwner(receiver);
//            tokenInfo.setUpdateTime(timestamp);
//            tokenInfo.setCreateTime(timestamp);
//            tokenInfo.setClassId(destinationClassID);
//            tokenInfo.setChainId(getChainId());
//            tokenInfo.setBaseClassId(baseClass);
//            tokenInfoService.add(tokenInfo);
//
//        }
//   }


    /**
     * ibc send
     *
     * @param message
     * @param log
     * @param txId
     * @param height
     * @param timestamp
     */
    public void nftIBCSend(JSONObject message, JSONObject log, String txId, String height, Long timestamp) {
        String classID = message.getString("class_id");
        List<String> tokenIds = message.getJSONArray("token_ids").toJavaList(String.class);
        String sender = message.getString("sender");
        String receiver = message.getString("receiver");

        //Parse event
        Map<String, JSONArray> eventsMap = eventToMap(log.getJSONArray("events"));


        //todo  nft token
//        for (String var : tokenIds) {
//            // Burn nft
//            JSONArray eventBurn = eventsMap.get("cosmos.nft.v1beta1.EventBurn");
//            if (eventBurn != null) {
//                tokenInfoService.burn(getChainId(), classID, var);
//
//            } else { // lock nft
//                Map<String, JSONArray> eventList = eventToMap(log.getJSONArray("events"));
//                JSONArray attributes = eventList.get("transfer");
//                Map<String, String> attribute = attributesDecoderToMap(attributes);
//                String tokenRecipient = attribute.get("recipient");
//                TokenInfo tokenInfo = tokenInfoService.queryOne(getChainId(), classID, var);
//                if (tokenInfo != null) {
//                    tokenInfoService.updateOwnerById(tokenInfo.getId(), receiver, timestamp);
//                }
//            }
//        }

        ibcSendTransaction(log, sender, receiver, classID, tokenIds, txId, height, timestamp);

    }


    public void swamIBCSend(JSONObject message, JSONObject log, String txId, String height, Long timestamp) {
        String sender = message.getString("sender");
        String contract = message.getString("contract");
        JSONObject send_nft = message.getJSONObject("msg").getJSONObject("send_nft");
        String tokenId = send_nft.getString("token_id");
        String msgCode = send_nft.getString("msg");
        String msg = new String(Base64.getDecoder().decode(msgCode));
        JSONObject msgJson = JSONObject.parseObject(msg);
        String receiver = msgJson.getString("receiver");
        ibcSendTransaction(log, sender, receiver, contract, Arrays.asList(tokenId), txId, height, timestamp);

    }

    public void ibcSendTransaction(JSONObject log, String sender, String receiver, String classId, List<String> tokenIds, String txId, String height, Long timestamp) {
        //Parse event
        Map<String, JSONArray> eventsMap = eventToMap(log.getJSONArray("events"));
        //get token transaction info
        JSONArray sendPacket = eventsMap.get("send_packet");
        Map<String, String> sendPacketMap = attributesToMap(sendPacket);
        String sequence = sendPacketMap.get("packet_sequence");
        String sourcePort = sendPacketMap.get("packet_src_port");
        String sourceChannel = sendPacketMap.get("packet_src_channel");
        String destinationPort = sendPacketMap.get("packet_dst_port");
        String destinationchannel = sendPacketMap.get("packet_dst_channel");
        String packetData = sendPacketMap.get("packet_data");

        JSONObject dataJson = JSONObject.parseObject(packetData);
        String sourceClassPath = dataJson.getString("classId");
        // nft base class id
        String[] strings = sourceClassPath.split("/");
        String baseClass = strings[strings.length - 1];

        IBCTransaction transaction = new IBCTransaction();
        transaction.setSourceTxid(txId);
        transaction.setSourceHeight(height);
        transaction.setSourceTime(timestamp);
        transaction.setDestinationChannel(destinationchannel);
        transaction.setDestinationPort(destinationPort);
        transaction.setReceiver(receiver);
        transaction.setSender(sender);
        transaction.setSourceClassID(classId);
        transaction.setTokenID(tokenIds);
        transaction.setData(packetData);
        transaction.setSequence(sequence);
        transaction.setSourceChannel(sourceChannel);
        transaction.setSourcePort(sourcePort);
        transaction.setSourceChainId(getChainId());
        transaction.setBaseClassId(baseClass);
        transaction.setSourceClassPath(sourceClassPath);
        iscTransactionService.add(transaction);
    }

    public void createDenom(JSONObject message, Long timestamp) {
        String id = message.getString("id");
        String symbol = message.getString("symbol");
        String name = message.getString("name");
        String description = message.getString("description");
        String schema = message.getString("schema");
        String sender = message.getString("sender");
        String uri = message.getString("uri");
        String uri_hash = message.getString("uri_hash");
        String data = message.getString("data");

        //is gon game
        Boolean isExist = accountService.isExist(sender);
        if (isExist) {
            ClassInfo classInfo = new ClassInfo();
            classInfo.setClassId(id);
            classInfo.setData(data);
            classInfo.setChainId(getChainId());
            classInfo.setCreater(sender);
            classInfo.setCreateTime(timestamp);
            classInfo.setDescription(description);
            classInfo.setName(name);
            classInfo.setSymbol(symbol);
            classInfo.setUri(uri);
            classInfo.setUriHash(uri_hash);
            classInfo.setSchema(schema);
            classInfoService.add(classInfo);

        }

    }


    public void mintNFT(JSONObject message, Long timestamp) {
        String id = message.getString("id");
        String denom_id = message.getString("denom_id");
        String name = message.getString("name");
        String uri = message.getString("uri");
        String data = message.getString("data");
        String sender = message.getString("sender");
        String recipient = message.getString("recipient");
        String uri_hash = message.getString("uri_hash");
        Boolean isExist = accountService.isExist(recipient);
        if (isExist) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setData(data);
            tokenInfo.setChainId(getChainId());
            tokenInfo.setTokenId(id);
            tokenInfo.setClassId(denom_id);
            tokenInfo.setBaseClassId(denom_id);
            tokenInfo.setCreateTime(timestamp);
            tokenInfo.setName(name);
            tokenInfo.setUri(uri);
            tokenInfo.setUriHash(uri_hash);
            tokenInfo.setOwner(recipient);
            tokenInfo.setUpdateTime(timestamp);
            tokenInfoService.add(tokenInfo);
        }
    }

    public void mintONFT(JSONObject message, Long timestamp) {
        String id = message.getString("id");
        String denom_id = message.getString("denom_id");
        String data = message.getString("data");
        String sender = message.getString("sender");
        String recipient = message.getString("recipient");
        String uri_hash = message.getJSONObject("metadata").getString("uri_hash");
        String name = message.getJSONObject("metadata").getString("name");
        String uri = message.getJSONObject("metadata").getString("media_uri");
        Boolean isExist = accountService.isExist(recipient);
        if (isExist) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setData(data);
            tokenInfo.setChainId(getChainId());
            tokenInfo.setTokenId(id);
            tokenInfo.setClassId(denom_id);
            tokenInfo.setBaseClassId(denom_id);
            tokenInfo.setCreateTime(timestamp);
            tokenInfo.setName(name);
            tokenInfo.setUri(uri);
            tokenInfo.setUriHash(uri_hash);
            tokenInfo.setOwner(recipient);
            tokenInfo.setUpdateTime(timestamp);
            tokenInfoService.add(tokenInfo);
        }
    }


    public void transferNFT(JSONObject message, Long timestamp) {
        String id = message.getString("id");
        String denom_id = message.getString("denom_id");
        String sender = message.getString("sender");
        String recipient = message.getString("recipient");
        TokenInfo tokenInfo = tokenInfoService.queryOne(getChainId(), denom_id, id);
        if (tokenInfo != null) {
            tokenInfoService.updateOwnerById(tokenInfo.getId(), recipient, timestamp);
        }
    }

    public Map<String, JSONArray> eventToMap(JSONArray events) {
        Map<String, JSONArray> map = new HashMap<>();
        for (int i = 0; i < events.size(); i++) {
            JSONObject jsonObject = events.getJSONObject(i);
            map.put(jsonObject.getString("type"), jsonObject.getJSONArray("attributes"));
        }
        return map;
    }


    public Map<String, String> attributesToMap(JSONArray attributes) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < attributes.size(); i++) {
            JSONObject jsonObject = attributes.getJSONObject(i);
            map.put(jsonObject.getString("key"), jsonObject.getString("value"));
        }
        return map;
    }

    public Map<String, String> attributesDecoderToMap(JSONArray attributes) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < attributes.size(); i++) {
            JSONObject jsonObject = attributes.getJSONObject(i);
            String key = new String(Base64.getDecoder().decode(jsonObject.getString("key")));
            String value = new String(Base64.getDecoder().decode(jsonObject.getString("value")));
            map.put(key, value);
        }
        return map;
    }

    public abstract String getChainId();

}
