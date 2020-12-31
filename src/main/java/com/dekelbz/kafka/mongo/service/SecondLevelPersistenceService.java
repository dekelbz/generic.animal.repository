package com.dekelbz.kafka.mongo.service;

import com.dekelbz.kafka.mongo.entity.CacheEntity;

import java.util.Optional;

public interface SecondLevelPersistenceService<T extends CacheEntity> {
    void save(String key, T entity);

    Optional<T> findById(Long id);
}
