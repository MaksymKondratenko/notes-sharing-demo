package org.mk.notessharedemo.note.domain.model;

import java.util.Optional;

public interface Note {
    NoteMetadata getMetadata();
    NoteData getData();

    default boolean isOwnedBy(UserFlyweight user) {
        return Optional.ofNullable(user)
                .map(UserFlyweight::getId)
                .map(id -> id.equals(getMetadata().getOwnerId()))
                .orElse(false);
    }
}
