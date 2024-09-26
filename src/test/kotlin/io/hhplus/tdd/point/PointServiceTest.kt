package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.exception.PointExceedException
import io.hhplus.tdd.exception.PointLackException
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

class PointServiceTest {
    private val service = PointService(
        pointHistoryTable = PointHistoryTable(),
        userPointTable = UserPointTable(),
        userPointLockMap = UserPointLock()
    )

    @Test
    @DisplayName("포인트가 부족할 경우의 예외처리 테스트")
    fun `Exception test for the lack of points after the subtract`() {
        assertThrows(PointLackException::class.java) {
            service.use(1, 100)
        }
    }

    @Test
    @DisplayName("포인트가 초과할 경우의 예외처리 테스트")
    fun `Exception test for the exceeded points after the addition`() {
        assertThrows(PointExceedException::class.java) {
            service.charge(1, 100_000_001)
        }
    }

}