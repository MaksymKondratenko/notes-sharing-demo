package org.mk.notessharedemo.note.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class SimpleNoteMetadata implements NoteMetadata {
    private final UUID id;
    private final UUID ownerId;
}
