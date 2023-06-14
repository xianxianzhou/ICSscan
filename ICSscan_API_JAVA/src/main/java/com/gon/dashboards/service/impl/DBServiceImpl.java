package com.gon.dashboards.service.impl;


import com.gon.dashboards.service.DBService;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class DBServiceImpl implements DBService {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public <T> void save(T entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public <T> void saveAll(List<T> list, Class<T> tClass) {
        mongoTemplate.insert(list, tClass);
    }


    @Override
    public <T> List<T> find(Map<String, Object> map, Class<T> tClass) {
        Query query = new Query();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
            }
        }
        List<T> user = mongoTemplate.find(query, tClass);
        return user;
    }

    @Override
    public <T> void update(String id, Map<String, Object> map, Class<T> tClass) {

        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            update.set(entry.getKey(), entry.getValue());

        }

        mongoTemplate.updateFirst(query, update, tClass);
    }


    @Override
    public <T> void delete(String id, Class<T> tClass) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, tClass);
    }

    @Override
    public <T> List<T> findByPage(Integer offset, Integer limit, Map<String, Object> map, Sort sort, Class<T> tClass) {
        Query query = new Query();
        if (map != null) {

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
            }
        }

        if (sort != null) {
            query.with(sort);
        }
        offset=offset-1;
        query.skip(offset).limit(limit);

        return mongoTemplate.find(query, tClass);
    }

    @Override
    public <T> List<T> customFind(Query query, Class<T> tClass) {
        return mongoTemplate.find(query, tClass);
    }

    @Override
    public <T> List<T> customFindByPage(Integer offset, Integer limit, Query query, Sort sort, Class<T> tClass) {
        offset=offset-1;
        query.skip(offset).limit(limit);
        if (sort != null) {
            query.with(sort);
        }

        return mongoTemplate.find(query, tClass);
    }

    @Override
    public <T> Long countDocuments(Class<T> tClass) {

        String collectionName = mongoTemplate.getCollectionName(tClass);
        MongoCollection<Document> documentMongoCollection = mongoTemplate.getCollection(collectionName);
        return documentMongoCollection.countDocuments();
    }

    @Override
    public  <T> void  deleteDocuments(Class<T> tClass){
        String collectionName = mongoTemplate.getCollectionName(tClass);
        mongoTemplate.dropCollection(collectionName);
    }

}
