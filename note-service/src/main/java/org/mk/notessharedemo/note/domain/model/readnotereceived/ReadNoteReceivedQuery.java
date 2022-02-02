package org.mk.notessharedemo.note.domain.model.readnotereceived;

import org.mk.notessharedemo.note.domain.model.NoteAccessPassword;

import java.util.UUID;

public interface ReadNoteReceivedQuery {
    UUID getNoteId();
    NoteAccessPassword getPassword();
}
