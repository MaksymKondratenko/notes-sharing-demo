package org.mk.notessharedemo.note.domain.model.readnoteowned;

import org.mk.notessharedemo.note.domain.model.UserFlyweight;

import java.util.UUID;

public interface ReadNoteOwnedQuery {
    UUID getNoteId();
    UserFlyweight getUser();
}
