package kr.co.ratelimiter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

class TokenLimiterTest: StringSpec({

    "Instant 기준 1초 계산하기" {
        val now = Instant.now()
        Thread.sleep(1000)
        val oneSecondLater = Instant.now()

        val elapsedTime = oneSecondLater.epochSecond - now.epochSecond

        elapsedTime shouldBe 1
    }

    "시간 기준 리필되는 토큰 계산하기" {
        val refillRate = 5000
        val now = Instant.now()
        Thread.sleep(2000)
        val elapsedTime = Instant.now().epochSecond - now.epochSecond

        val tokenAdd = (elapsedTime * refillRate)

        tokenAdd shouldBe 10000
    }
})