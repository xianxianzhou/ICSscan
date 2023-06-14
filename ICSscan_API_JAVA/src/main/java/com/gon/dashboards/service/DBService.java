package com.gon.dashboards.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;


public interface DBService {

    <T> void save(T entity);

    <T> void saveAll(List<T> list, Class<T> tClass);

    <T> List<T> find(Map<String, Object> map, Class<T> tClass);

    <T> void update(String id, Map<String, Object> map, Class<T> tClass);

    <T> void delete(String id, Class<T> tClass);

    <T> List<T> findByPage(Integer offset, Integer limit, Map<String, Object> map, Sort sort, Class<T> tClass);

    <T> List<T> customFind(Query query, Class<T> tClass);

    <T> List<T> customFindByPage(Integer offset, Integer limit, Query query, Sort sort, Class<T> tClass);

    <T> Long countDocuments(Class<T> tClass);

    <T> void deleteDocuments(Class<T> tClass);
}
