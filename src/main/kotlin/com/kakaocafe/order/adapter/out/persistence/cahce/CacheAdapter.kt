package com.kakaocafe.order.adapter.out.persistence.cahce

import com.kakaocafe.order.application.port.out.CachePort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class CacheAdapter(
    private val redisTemplate: RedisTemplate<String, String>
) : CachePort {
    override fun put(pair: Pair<String, String>, ttl: Long, timeUnit: TimeUnit): Boolean? {
        val (key, value) = pair
        return redisTemplate.opsForValue().setIfAbsent(key, value, ttl, timeUnit)
    }

    override fun delete(key: String) {
        redisTemplate.delete(key)
    }
}