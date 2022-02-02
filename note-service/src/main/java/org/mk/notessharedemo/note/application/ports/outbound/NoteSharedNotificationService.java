package org.mk.notessharedemo.note.application.ports.outbound;

import org.mk.notessharedemo.note.domain.model.SharedNoteMetadata;

public interface NoteSharedNotificationService {
    void sendNotification(SharedNoteMetadata noteMetadata);
}
