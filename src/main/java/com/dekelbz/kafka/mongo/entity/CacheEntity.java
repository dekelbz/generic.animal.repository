package com.dekelbz.kafka.mongo.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS,
        property = "@class")
public abstract class CacheEntity {
    @Id
    private Long id;
}
