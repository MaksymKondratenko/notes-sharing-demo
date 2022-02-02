package org.mk.notessharedemo.note.domain.model.readnoteowned;

import lombok.Builder;
import lombok.Getter;
import org.mk.notessharedemo.note.domain.model.UserFlyweight;

import java.util.UUID;

@Builder
@Getter
public class ReadNoteOwnedQueryImpl implements ReadNoteOwnedQuery {
    private final UUID noteId;
    private final UserFlyweight user;
}
