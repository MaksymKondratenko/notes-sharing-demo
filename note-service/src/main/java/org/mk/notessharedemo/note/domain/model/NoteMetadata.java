package org.mk.notessharedemo.note.domain.model;

import java.util.UUID;

public interface NoteMetadata {
    UUID getId();
    UUID getOwnerId();
}
