package org.mk.notessharedemo.note.application.ports.outbound;

import org.mk.notessharedemo.note.domain.model.Note;

import java.util.Collection;
import java.util.UUID;

public interface NoteRepositoryFacade {
    Collection<Note> findAllByOwnerId(UUID ownerId);
}
