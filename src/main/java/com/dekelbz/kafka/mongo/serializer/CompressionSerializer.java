package com.dekelbz.kafka.mongo.serializer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.xerial.snappy.Snappy;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class CompressionSerializer<T> implements RedisSerializer<T> {

    private final RedisSerializer<T> innerSerializer;

    @SneakyThrows
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return null;
        }
        return Snappy.compress(Objects.requireNonNull(innerSerializer.serialize(t)));
    }

    @SneakyThrows
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        return innerSerializer.deserialize(Snappy.uncompress(bytes));
    }
}
