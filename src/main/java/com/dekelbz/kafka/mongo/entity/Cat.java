package com.dekelbz.kafka.mongo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.redis.core.RedisHash;

@EqualsAndHashCode(callSuper = true)
@Data
//@RedisHash(value = "animal", timeToLive = 20 * 60)
public class Cat extends Animal{
    private int color;
    private int beauty;
}
