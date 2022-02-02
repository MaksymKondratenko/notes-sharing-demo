package org.mk.notessharedemo.note.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class SharedNote implements Note {
    private final SimpleNoteData data;
    private final SharedNoteMetadata metadata;

    public static SharedNote fromNote(Note note, NoteAccessPassword password, Long ttl, String recipientEmail) {
        var meta = new SharedNoteMetadataImpl(note.getMetadata().getId(), note.getMetadata().getOwnerId(), ttl, password.getValue(), recipientEmail);
        return new SharedNote(SimpleNoteData.from(note.getData()), meta);
    }
}
