package com.gon.dashboards.service.impl;

import com.gon.dashboards.entity.IBCTransaction;
import com.gon.dashboards.service.DBService;
import com.gon.dashboards.service.IBCTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;

@Service

public class IBCTransactionServiceImpl implements IBCTransactionService {


    @Autowired
    private DBService dbService;


    @Override
    @Transactional
    public void add(IBCTransaction transaction) {
        if (transaction != null) {

            //Query whether there is source or destination data,
            Map<String, Object> findKey = new HashMap<>();
            findKey.put("sequence", transaction.getSequence());
            findKey.put("sourcePort", transaction.getSourcePort());
            findKey.put("sourceChannel", transaction.getSourceChannel());
            findKey.put("destinationPort", transaction.getDestinationPort());
            findKey.put("destinationChannel", transaction.getDestinationChannel());
            List<IBCTransaction> transactionList = dbService.find(findKey, IBCTransaction.class);

            // update data
            if (transactionList != null && transactionList.size() > 0) {
                IBCTransaction db = transactionList.get(0);
                if (db.getSourceTxid() != null && db.getDestinationTxid() != null) {
                    return;
                }
                Map<String, Object> map = updateData(db, transaction);
                if (map.size()>0){
                    dbService.update(db.getId(), map, IBCTransaction.class);
                }

            } else {
                //add data
                dbService.save(transaction);
            }

        }

    }

    @Override
    public List<IBCTransaction> getCrossChainListByTxid(String txid) {
        Query query = new Query();
        Criteria criteria = new Criteria().orOperator(Criteria.where("destinationTxid").is(txid), Criteria.where("sourceTxid").is(txid));
        query.addCriteria(criteria);
        query.addCriteria(Criteria.where("sourceTxid").ne("").ne(null));
        query.addCriteria(Criteria.where("destinationTxid").ne("").ne(null));

        Sort.Order order=Sort.Order.desc("sourceTime");
        Sort sort=Sort.by(order);
        query.with(sort);
        List<IBCTransaction> iscTransactionList = dbService.customFind(query, IBCTransaction.class);
        //query tokenID all cross list
//        if (iscTransactionList != null && iscTransactionList.size() > 0) {
//            String baseClass = iscTransactionList.get(0).getBaseClassId();
//            List<String> tokenList = iscTransactionList.get(0).getTokenID();
//            query = new Query();
//            Criteria criteria1 = Criteria.where("baseClassId").is(baseClass).andOperator(Criteria.where("tokenID").in(tokenList.stream().toArray()));
//            query.addCriteria(criteria1);
//            List<ISCTransaction> list = dbService.customFind(query, ISCTransaction.class);
//            Collections.sort(list, new Comparator<ISCTransaction>() {
//                public int compare(ISCTransaction a, ISCTransaction b) {
//                    if (a.getDestinationTime() != null && b.getDestinationTime() != null) {
//                        return a.getDestinationTime().compareTo(b.getDestinationTime());
//                    } else if (a.getSourceTime() != null && b.getSourceTime() != null) {
//                        return a.getSourceTime().compareTo(b.getSourceTime());
//                    }
//                    return 0;
//                }
//            });
//            return list;
//
//        }

        return iscTransactionList;
    }

    @Override
    public List<IBCTransaction> getCrossChainListByAddress(String address) {
        Query query = new Query();
        Criteria criteria = new Criteria().orOperator(Criteria.where("sender").is(address), Criteria.where("receiver").is(address));
        query.addCriteria(criteria);
        query.addCriteria(Criteria.where("sourceTxid").ne("").ne(null));
        query.addCriteria(Criteria.where("destinationTxid").ne("").ne(null));
        Sort.Order order=Sort.Order.desc("sourceTime");
        Sort sort=Sort.by(order);
        query.with(sort);
        List<IBCTransaction> iscTransactionList = dbService.customFind(query, IBCTransaction.class);
        return iscTransactionList;
    }


    @Override
    public Long count() {
        return dbService.countDocuments(IBCTransaction.class);
    }


    public static Map<String, Object> updateData(Object obj1, Object obj2) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj1.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj1);
                if (value == null) {
                    Object value2 = field.get(obj2);
                    if (value2 != null) {
                        map.put(field.getName(), value2);
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public List<IBCTransaction> queryByPage(String search, Integer page, Integer size) {
        Query query = new Query();
        if (!StringUtils.isEmpty(search)) {
           // Pattern pattern = Pattern.compile("^.*" + search + ".*$", Pattern.CASE_INSENSITIVE);
            Criteria criteria = new Criteria().orOperator(Criteria.where("sender").is(search),
                    Criteria.where("receiver").is(search),
                    Criteria.where("tokenID").in(search),
                    Criteria.where("destinationClassID").is(search),
                    Criteria.where("sourceChannel").is(search),
                    Criteria.where("sourceChainId").is(search),
                    Criteria.where("destinationPort").is(search),
                    Criteria.where("destinationchannel").is(search),
                    Criteria.where("destinationChainId").is(search),
                    Criteria.where("baseClassId").is(search));
            query.addCriteria(criteria);

        }
        query.addCriteria(Criteria.where("sourceTxid").ne("").ne(null));
        query.addCriteria(Criteria.where("destinationTxid").ne("").ne(null));
        Sort.Order order=Sort.Order.desc("sourceTime");
        Sort sort=Sort.by(order);
        query.with(sort);
        List<IBCTransaction> list = dbService.customFindByPage(page, size, query, null, IBCTransaction.class);
        return list;
    }

}
