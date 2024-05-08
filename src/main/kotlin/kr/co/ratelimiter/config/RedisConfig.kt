package kr.co.ratelimiter.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.ratelimiter.properties.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    val properties: RedisProperties
) {
    @Bean
    fun redisConnection(): LettuceConnectionFactory {
        val config = RedisStandaloneConfiguration(properties.host, properties.port)
        return LettuceConnectionFactory(config)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()

        val objectMapper = ObjectMapper()

        objectMapper.apply {
            configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
        }

        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(Object::class.java)

        redisTemplate.apply {
            connectionFactory = redisConnection()
            keySerializer = StringRedisSerializer()
            valueSerializer = jackson2JsonRedisSerializer
            afterPropertiesSet()
        }

        return redisTemplate
    }
}