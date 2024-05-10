package kr.co.ratelimiter.redis

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kr.co.ratelimiter.service.TokenLimitService
import org.springframework.data.redis.core.RedisTemplate


class RedisTest: BehaviorSpec({
    val redisTemplate = mockk<RedisTemplate<String, Any>>(relaxed = true)
    val tokenLimitService = TokenLimitService(redisTemplate)

    Given("토큰이 300,000개가 있을 때") {
        val remainToken = 300000L
        every { redisTemplate.opsForValue().get(any()) } returns remainToken

        When("가져가는 토큰이 전체 토큰보다 작을 때") {
            val acquireToken = 50000L

            val result = tokenLimitService.acquireToken(acquireToken)

            Then("요청 접근 허용") {
                result shouldBe true
            }
        }

        When("가져가는 토큰이 전체 토큰보다 클 때") {
            val acquireToken = 350000L

            val result = tokenLimitService.acquireToken(acquireToken)

            Then("요청 제한") {
                result shouldBe false
            }
        }
    }
})