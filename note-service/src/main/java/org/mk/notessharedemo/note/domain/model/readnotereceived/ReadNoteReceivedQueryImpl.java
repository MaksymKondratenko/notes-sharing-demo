package org.mk.notessharedemo.note.domain.model.readnotereceived;

import lombok.Builder;
import lombok.Getter;
import org.mk.notessharedemo.note.domain.model.NoteAccessPassword;

import java.util.UUID;

@Getter
@Builder
public class ReadNoteReceivedQueryImpl implements ReadNoteReceivedQuery{
    private final UUID noteId;
    private final NoteAccessPassword password;
}
