package com.dekelbz.kafka.mongo.dao;

import com.dekelbz.kafka.mongo.entity.CacheEntity;
import com.dekelbz.kafka.mongo.service.SecondLevelPersistenceService;
import com.esotericsoftware.kryo.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public abstract class GenericRedisDaoImpl<T extends CacheEntity> implements RedisDao<T> {

    private final RedisTemplate<String, T> redisTemplate;
    private final Class<T> type;
    private final boolean secondLevelCache;
    private final CrudRepository<T, Long> secondLevelRepository;

    @Override
    public T save(T entity) {
        final var saveEntity = saveInternal(entity);
        if (secondLevelCache) {
            secondLevelRepository.save(saveEntity);
        }
        return saveEntity;
    }

    private T saveInternal(T entity) {
        final var entityOps = redisTemplate.boundValueOps(getKey(entity));
        if (secondLevelCache) {
            entityOps.set(entity, 20, TimeUnit.MINUTES);
        } else {
            entityOps.set(entity);
        }
        return entity;
    }

    private String getKey(T entity) {
        final var optionalEntityId = Optional.ofNullable(entity.getId());
        final var entityId = optionalEntityId.orElseGet(() -> redisTemplate.boundValueOps(type.getSimpleName() + "-key").increment());

        entity.setId(entityId);
        final var key = getKey(entityId);
        verifyKey(optionalEntityId.isPresent(), key);
        return key;
    }

    private void verifyKey(boolean exists, String key) {
        if (exists && Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
           // throw new WrongIdGivenException();
        }
    }

    private String getKey(Long entityId) {
        return String.format("%s-%d", type.getSimpleName(), entityId);
    }


    @Override
    public Optional<T> findById(Long id) {
        var entity = Optional.ofNullable(redisTemplate.boundValueOps(getKey(id)).get());
        if (secondLevelCache && entity.isEmpty()) {
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

