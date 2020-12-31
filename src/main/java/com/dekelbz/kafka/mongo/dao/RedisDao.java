package com.dekelbz.kafka.mongo.dao;


import com.dekelbz.kafka.mongo.entity.CacheEntity;

import java.util.Optional;

public interface RedisDao<T extends CacheEntity> {

    T save(T entity);

    Optional<T> findById(Long id);

    void deleteById(Long id);
}
