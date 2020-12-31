package com.dekelbz.kafka.mongo.dao;


import com.dekelbz.kafka.mongo.entity.Animal;
import com.dekelbz.kafka.mongo.service.SecondLevelPersistenceService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AnimalRedisDaoImpl extends GenericRedisDaoImpl<Animal> {


    public AnimalRedisDaoImpl(RedisTemplate<String, Animal> redisTemplate, MongoRepository<Animal, Long> repository) {
        super(redisTemplate, Animal.class, true, repository);
    }
}
