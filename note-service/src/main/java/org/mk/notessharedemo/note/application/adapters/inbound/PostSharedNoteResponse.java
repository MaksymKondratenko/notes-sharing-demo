package org.mk.notessharedemo.note.application.adapters.inbound;

import lombok.Getter;
import org.mk.notessharedemo.note.domain.model.SharedNote;
import org.mk.notessharedemo.note.domain.model.SharedNoteMetadata;

import java.util.UUID;

@Getter
class PostSharedNoteResponse {
    private final UUID sharedNoteId;
    private final String password;

    private PostSharedNoteResponse(UUID sharedNoteId, String password) {
        this.sharedNoteId = sharedNoteId;
        this.password = password;
    }


    static PostSharedNoteResponse of(SharedNote sharedNote) {
        SharedNoteMetadata metadata = sharedNote.getMetadata();
        return new PostSharedNoteResponse(metadata.getId(), metadata.getAssociatedKey());
    }
}
