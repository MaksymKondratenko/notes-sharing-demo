package org.mk.notessharedemo.note.application.adapters.inbound;

import lombok.Getter;
import org.mk.notessharedemo.note.domain.model.Note;

class GetNoteByIdResponse {
    @Getter
    private final Note note;

    private GetNoteByIdResponse(Note note) {
        this.note = note;
    }

    static GetNoteByIdResponse of(Note note) {
        return new GetNoteByIdResponse(note);
    }
}
