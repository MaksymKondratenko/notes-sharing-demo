package org.mk.notessharedemo.note.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "notification")
@ConstructorBinding
@Getter
@RequiredArgsConstructor
public final class NotificationProps {
    private final EmailProps email;

    @Getter
    @RequiredArgsConstructor
    public static final class EmailProps {
        private final String hostname;
        private final String port;
        private final String serviceAddress;
    }
}
