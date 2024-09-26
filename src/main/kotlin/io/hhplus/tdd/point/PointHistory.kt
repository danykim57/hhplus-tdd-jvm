package io.hhplus.tdd.point

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Positive

data class PointHistory(
    val id: Long,
    @field:Positive(message = "id는 0보다 커야 합니다.")
    val userId: Long,
    val type: TransactionType,
    @field:Min(0, message = "point는 0이거나 양수여야 합니다.")
    val amount: Long,
    val timeMillis: Long,
)

/**
 * 포인트 트랜잭션 종류
 * - CHARGE : 충전
 * - USE : 사용
 */
enum class TransactionType {
    CHARGE, USE
}