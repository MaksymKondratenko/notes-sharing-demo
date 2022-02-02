package org.mk.notessharedemo.note.application.ports.outbound;

import org.mk.notessharedemo.note.domain.model.SharedNote;

public interface SharedNoteRepositoryFacade {
    SharedNote save(SharedNote note);
}
