package org.mk.notessharedemo.note.domain.model;

import lombok.Getter;

import java.util.UUID;

public class UserFlyweight {
    @Getter
    private final UUID id;

    private UserFlyweight(UUID id) {
        this.id = id;
    }

    public static UserFlyweight of(UUID id) {
        return new UserFlyweight(id);
    }
}
