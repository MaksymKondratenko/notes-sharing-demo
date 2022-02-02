package org.mk.notessharedemo.note.application.adapters

import org.mk.notessharedemo.note.application.RedisProps
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@TestConfiguration
class RedisTestConfiguration {

    private RedisServer redisServer

    RedisTestConfiguration(RedisProps props) {
        this.redisServer = RedisServer.builder()
                .port(props.server.port)
                .setting("maxmemory 128M") //maxheap 128M
                .build()
    }

    @PostConstruct
    void postConstruct() {
        redisServer.start()
    }

    @PreDestroy
    void preDestroy() {
        if (Objects.nonNull redisServer) redisServer.stop()
    }
}
