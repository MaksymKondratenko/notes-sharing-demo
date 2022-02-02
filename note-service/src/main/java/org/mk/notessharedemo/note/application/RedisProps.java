package org.mk.notessharedemo.note.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "redis")
@ConstructorBinding
@Getter
@RequiredArgsConstructor
public final class RedisProps {
    private final Server server;

    @Getter
    @RequiredArgsConstructor
    static final class Server {
        private final String hostname;
        private final int port;
    }
}
