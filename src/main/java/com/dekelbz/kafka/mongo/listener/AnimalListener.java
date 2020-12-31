package com.dekelbz.kafka.mongo.listener;

import com.dekelbz.kafka.mongo.dao.RedisDao;
import com.dekelbz.kafka.mongo.entity.Animal;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimalListener {

    private final RedisDao<Animal> animalDao;
    private final KafkaTemplate<String, Animal> kafkaTemplate;

    @KafkaListener(topics = "animal-save")
    public void saveAnimal(Animal animal) {
        sendRetrievedAnimal(animalDao.save(animal));
    }

    private void sendRetrievedAnimal(Animal animal) {
        kafkaTemplate.send("animal-retrieved-by-id", animal);
    }

    @KafkaListener(topics = "animal_delete")
    public void deleteAnimal(Long id) {
        animalDao.deleteById(id);
    }

    @KafkaListener(topics = "animal-get-by-id")
    public void getById(Long id) {
        sendRetrievedAnimal(animalDao.findById(id).orElse(null));
    }

}
