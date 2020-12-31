package com.dekelbz.kafka.mongo.service;

import com.dekelbz.kafka.mongo.entity.CacheEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecondLevelPersistenceServiceImpl<T extends CacheEntity>  implements SecondLevelPersistenceService<T>  {

    private final KafkaTemplate<String, T> kafkaTemplate;

    @Override
    public void save(String key, T entity) {
        kafkaTemplate.sendDefault(key, entity);
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.empty();

    }
}
