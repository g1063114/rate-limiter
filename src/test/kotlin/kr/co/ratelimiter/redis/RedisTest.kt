package kr.co.ratelimiter.redis

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.redis.core.RedisTemplate


class RedisTest: BehaviorSpec({
    val redisTemplate = mockk<RedisTemplate<String, Any>>()

    Given("토큰이 300,000개가 있을 때") {
        every { redisTemplate.opsForValue().get("token") } returns 300000

        When("토큰이 충분할 때") {
            val acquireToken = 5000
            Then("요청 접근 허용") {

            }
        }
    }
})