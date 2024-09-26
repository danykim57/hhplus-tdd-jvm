package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.exception.PointExceedException
import io.hhplus.tdd.exception.PointLackException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Service
class PointService(
    private val pointHistoryTable: PointHistoryTable,
    private val userPointTable: UserPointTable,
    private val userPointLockMap: UserPointLock
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun findPointById(id: Long): UserPoint {
        return userPointTable.selectById(id);
    }

    fun findAllHistoriesById(id: Long): List<PointHistory> {
        return pointHistoryTable.selectAllByUserId(id)
    }

    @Throws(PointExceedException::class)
    fun charge(id: Long, amount: Long): UserPoint {
        val reentrantLock:ReentrantLock = userPointLockMap.getLock(id)
        reentrantLock.withLock {
            val userPoint = findPointById(id)
            val expectedAmount = userPoint.point + amount
            if (expectedAmount > 100_000_000) {
                throw PointExceedException("포인트가 1억이 최대 잔고 입니다.")
            }
            lateinit var result: UserPoint
            try {
                result = userPointTable.insertOrUpdate(id, expectedAmount)
                pointHistoryTable.insert(result.id, expectedAmount, TransactionType.CHARGE, result.updateMillis)
                log.info("Point: " + result.point);
            } catch (e: Throwable) {
                log.warn("Exception: {}", e.message, e)
            }
            return result
        }
    }

    @Throws(PointLackException::class)
    fun use(id: Long, amount: Long): UserPoint {
        val reentrantLock:ReentrantLock = userPointLockMap.getLock(id)
        reentrantLock.withLock {
            val userPoint = findPointById(id)
            val expectedAmount = userPoint.point - amount
            if (expectedAmount < 0) {
                throw PointLackException("포인트가 부족합니다.")
            }
            lateinit var result: UserPoint
            try {
                result = userPointTable.insertOrUpdate(id, expectedAmount)
                pointHistoryTable.insert(result.id, result.point, TransactionType.USE, result.updateMillis)
            } catch (e: Throwable) {
                log.warn("Exception: {}", e.message, e)
            }
            return result
        }
    }

}