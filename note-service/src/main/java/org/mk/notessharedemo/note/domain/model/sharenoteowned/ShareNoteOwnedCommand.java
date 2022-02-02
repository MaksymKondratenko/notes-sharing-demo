package org.mk.notessharedemo.note.domain.model.sharenoteowned;

import org.mk.notessharedemo.note.domain.model.UserFlyweight;

import java.util.UUID;

public interface ShareNoteOwnedCommand {
    UUID getNoteId();
    Long getTtlOrDefault(Long defaultTtl);
    String getRecipientEmail();
    UserFlyweight getUser();
}
