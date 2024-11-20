package com.sparta.ssaktium.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;


    }
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 클러스터 노드 주소 설정
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration()
                .clusterNode("127.0.0.1", 7000)  // 첫 번째 노드 주소
                .clusterNode("127.0.0.1", 7001)  // 두 번째 노드 주소
                .clusterNode("127.0.0.1", 7002)  // 세 번째 노드 주소
                .clusterNode("127.0.0.1", 7003)  // 네 번째 노드 주소
                .clusterNode("127.0.0.1", 7004)  // 다섯 번째 노드 주소
                .clusterNode("127.0.0.1", 7005); // 여섯 번째 노드 주소

        // LettuceConnectionFactory를 사용하여 클러스터 연결을 설정
        return new LettuceConnectionFactory(clusterConfiguration);
    }

}