package com.dekelbz.kafka.mongo.service;


import com.dekelbz.kafka.mongo.entity.Animal;

import java.util.Optional;

public interface AnimalService {
     Long save(Animal animal);

    Optional<Animal> getById(Long id);

    boolean delete(Long id);
}
