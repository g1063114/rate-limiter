package kr.co.ratelimiter.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class TokenLimitService(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    private var lastRefillTime: Instant = Instant.now()
    init {
        redisTemplate.opsForValue().set("token", 300000)
    }

    @Transactional
    fun acquireToken(token: Long): Boolean {
        refillToken()
        val remainToken = redisTemplate.opsForValue().get("token")
        if ( remainToken as Long > token) {
            redisTemplate.opsForValue().set("token", remainToken - token)
            return true
        }
        return false
    }

    @Transactional
    fun refillToken() {
        val now = Instant.now()
        val elapsed = now.epochSecond - lastRefillTime.epochSecond
        val tokenAdd = (elapsed * 5000).toInt()
        val token = redisTemplate.opsForValue().get("token") as Long
        redisTemplate.opsForValue().set("token", minOf(token + tokenAdd, CAPACITY))
        lastRefillTime = now
    }

    companion object  {
        const val CAPACITY: Long = 300000L
    }
}