package com.dekelbz.kafka.mongo.repository;

import com.dekelbz.kafka.mongo.entity.Animal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("secondLevel")
public interface AnimalMongoRepository extends MongoRepository<Animal, Long> {
}
