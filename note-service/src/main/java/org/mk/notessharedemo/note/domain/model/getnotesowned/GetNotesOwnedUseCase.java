package org.mk.notessharedemo.note.domain.model.getnotesowned;

import org.mk.notessharedemo.note.domain.model.Note;

import java.util.Collection;

public interface GetNotesOwnedUseCase {
    Collection<Note> runFor(GetNotesOwnedQuery command);
}
