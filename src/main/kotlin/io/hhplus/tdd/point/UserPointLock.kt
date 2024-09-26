package io.hhplus.tdd.point

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

@Component
class UserPointLock {
    val lockMap: ConcurrentHashMap<Long, ReentrantLock> = ConcurrentHashMap()

    fun getLock(id: Long): ReentrantLock {
        if (!lockMap.containsKey(id)) {
            lockMap.put(id, ReentrantLock())
        }
        return lockMap[id]!!
    }
}