package com.dekelbz.kafka.mongo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayOutputStream;

public class KryoSerializer<T> implements RedisSerializer<T> {

    private static final ThreadLocal<Kryo> KRYO = ThreadLocal.withInitial(() -> {
        var kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(T t) throws SerializationException {
        var outputStream = new ByteArrayOutputStream();
        try (var output = new Output(outputStream)) {
            KRYO.get().writeClassAndObject(output, t);
        }
        return outputStream.toByteArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(byte[] bytes) throws SerializationException {
        return (T) KRYO.get().readClassAndObject(new Input(bytes));
    }

}
