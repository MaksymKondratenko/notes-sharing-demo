package org.mk.notessharedemo.note.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class SimpleNote implements Note {
    private final SimpleNoteData data;
    private final SimpleNoteMetadata metadata;
}
