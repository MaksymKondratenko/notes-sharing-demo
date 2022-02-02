package org.mk.notessharedemo.note.domain.model.readnotereceived;

import org.mk.notessharedemo.note.domain.model.Note;

public interface ReadNoteReceivedUseCase {
    Note runFor(ReadNoteReceivedQuery query);
}
