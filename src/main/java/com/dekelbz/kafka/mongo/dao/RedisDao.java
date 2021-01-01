package com.dekelbz.kafka.mongo.dao;

import com.dekelbz.kafka.mongo.entity.CacheEntity;

import java.util.Optional;

public abstract class RedisDao<T extends CacheEntity> {

    public abstract T save(T entity);

    public abstract Optional<T> findById(Long id);

    public abstract void deleteById(Long id);
}
