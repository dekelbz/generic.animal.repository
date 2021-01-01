package com.dekelbz.kafka.mongo.dao;

import com.dekelbz.kafka.mongo.entity.CacheEntity;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class RedisDaoImpl<T extends CacheEntity> extends RedisDao<T> {

    @Autowired
    private RedisTemplate<String, T> redisTemplate;
    @Autowired
    private CrudRepository<T, Long> secondLevelRepository;

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final String namespace = calculateNamespace();

    private String calculateNamespace() {
        Type superclass = getClass().getGenericSuperclass();
        final var typeName = ((ParameterizedType) superclass).getActualTypeArguments()[0]
                .getTypeName();
        return typeName.substring(typeName.lastIndexOf('.') + 1);
    }

    @Override
    public T save(T entity) {
        verifyExistingEntity(entity);
        saveInternal(entity);
        secondLevelRepository.save(entity);
        return entity;
    }

    private void saveInternal(T entity) {
        redisTemplate.boundValueOps(getKey(entity)).set(entity, 1, TimeUnit.MINUTES);
    }

    private void verifyExistingEntity(T entity) {
        final var id = entity.getId();
        if (id != null && !secondLevelRepository.existsById(id)) {
            throw new WrongIdGivenException();
        }
    }

    private String getKey(T entity) {
        final var entityId = Optional.ofNullable(entity.getId())
                .orElseGet(() -> redisTemplate.boundValueOps(getNamespace() + "-key").increment());

        entity.setId(entityId);
        return getKey(entityId);
    }

    private String getKey(Long entityId) {
        return String.format("%s-%d", getNamespace(), entityId);
    }


    @Override
    public Optional<T> findById(Long id) {
        var entity = Optional.ofNullable(redisTemplate.boundValueOps(getKey(id)).get());
        if (entity.isEmpty()) {
            entity = secondLevelRepository.findById(id);
            entity.ifPresent(this::saveInternal);
        }
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        redisTemplate.delete(getKey(id));
    }

}

