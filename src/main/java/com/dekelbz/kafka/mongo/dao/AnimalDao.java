package com.dekelbz.kafka.mongo.dao;

import com.dekelbz.kafka.mongo.entity.Animal;
import org.springframework.stereotype.Repository;

@Repository
public class AnimalDao extends RedisDaoImpl<Animal> {
}
