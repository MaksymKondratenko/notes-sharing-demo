package org.mk.notessharedemo.note.domain.model.sharenoteowned;

import org.mk.notessharedemo.note.domain.model.SharedNote;

public interface ShareNoteOwnedUseCase {
    SharedNote runFor(ShareNoteOwnedCommand command);
}
