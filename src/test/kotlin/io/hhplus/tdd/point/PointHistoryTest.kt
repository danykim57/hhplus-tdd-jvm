package io.hhplus.tdd.point

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

class PointHistoryTest {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @DisplayName("Id가 0 일 때 유효성 검사에 실패한다.")
    @Test
    fun `UserPoint Construction should be failed when userId is 0`() {
        val pointHistory = PointHistory(1, 0, TransactionType.CHARGE, 0, timeMillis = System.currentTimeMillis())
        val violations = validator.validate(pointHistory)

        assertEquals(1, violations.size)
        assertEquals("id는 0보다 커야 합니다.", violations.first().message)
    }

    @DisplayName("Id가 음수 일 때 유효성 검사에 실패한다.")
    @Test
    fun `UserPoint Construction should be failed when id is negative number`() {
        val pointHistory = PointHistory(1, -1, TransactionType.CHARGE, 0, timeMillis = System.currentTimeMillis())
        val violations = validator.validate(pointHistory)

        assertEquals(1, violations.size)
        assertEquals("id는 0보다 커야 합니다.", violations.first().message)
    }

    @DisplayName("point가 음수 일 때 유효성 검사에 실패한다.")
    @Test
    fun `UserPoint Construction should be failed when point is negative number`() {
        val pointHistory = PointHistory(1, 1, TransactionType.CHARGE, -1, timeMillis = System.currentTimeMillis())
        val violations = validator.validate(pointHistory)

        assertEquals(1, violations.size)
        assertEquals("point는 0이거나 양수여야 합니다.", violations.first().message)
    }
}