package io.hhplus.tdd.point

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Positive
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock


data class UserPoint(
    @field:Positive(message = "id는 0보다 커야 합니다.")
    val id: Long,
    @field:Min(0, message = "point는 0이거나 양수여야 합니다.")
    val point: Long,
    //테이블 클래스에서 System.currentTimeMillis()로 설정됨
    val updateMillis: Long,
)
