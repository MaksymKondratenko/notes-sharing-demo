package org.mk.notessharedemo.note.domain.model.readnoteowned;

import org.mk.notessharedemo.note.domain.model.Note;

public interface ReadNoteOwnedUseCase {
    Note runFor(ReadNoteOwnedQuery command);
}
