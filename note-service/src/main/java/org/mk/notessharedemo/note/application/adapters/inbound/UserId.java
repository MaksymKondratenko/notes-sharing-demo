package org.mk.notessharedemo.note.application.adapters.inbound;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode
@RequiredArgsConstructor
public class UserId {
    private final UUID id;

    public UUID get() {
        return id;
    }
}
