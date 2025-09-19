package com.example.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;



@Configuration
public class RedisConfig {

    /**
     * 애플리케이션 전반에서 사용할 RedisTemplate Bean을 생성합니다.
     * <p>
     * RedisTemplate은 Redis 서버에 명령을 보내는 역할을 하는 핵심 클래스입니다.
     * 여기서는 Key와 Value 모두 일반 문자열(String)로 처리하도록 설정합니다.
     * 조회수 카운팅과 같은 간단한 작업에는 이 설정이 가장 효율적입니다.
     * </p>
     *
     * @param connectionFactory Spring Boot가 자동으로 생성해주는 Redis 연결 팩토리
     * @return Key와 Value가 String으로 직렬화되도록 설정된 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Key와 Value 모두 String으로 직렬화하도록 설정합니다.
        // 이렇게 하면 Redis CLI에서 데이터를 알아보기 쉽습니다.
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        
        // Hash 자료구조를 사용할 경우를 대비하여 Hash Key와 Value의 직렬화 방식도 설정합니다.
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        
        template.afterPropertiesSet();
        return template;
    }
}