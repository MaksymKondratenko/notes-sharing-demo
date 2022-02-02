package org.mk.notessharedemo.note.application.adapters.inbound;

import lombok.Getter;
import org.mk.notessharedemo.note.domain.model.Note;
import org.mk.notessharedemo.note.domain.model.NoteData;

class GetSharedNoteByIdResponse {
    @Getter
    private final NoteData note;

    private GetSharedNoteByIdResponse(NoteData note) {
        this.note = note;
    }

    static GetSharedNoteByIdResponse of(Note note) {
        return new GetSharedNoteByIdResponse(note.getData());
    }
}
