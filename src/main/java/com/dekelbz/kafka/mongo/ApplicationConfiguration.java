package com.dekelbz.kafka.mongo;

import com.dekelbz.kafka.mongo.serializer.CompressionSerializer;
import com.dekelbz.kafka.mongo.serializer.KryoSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.support.converter.BytesJsonMessageConverter;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public BytesJsonMessageConverter jsonMessageConverter() {
        return new BytesJsonMessageConverter();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(@Value("${spring.redis.host}") String host) {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host));
    }

    @Bean
    public <T> RedisSerializer<T> redisSerializer() {
        return new CompressionSerializer<>(new KryoSerializer<>());
    }

    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory connectionFactory,
                                                    RedisSerializer<V> valueSerializer) {
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;
    }

}
