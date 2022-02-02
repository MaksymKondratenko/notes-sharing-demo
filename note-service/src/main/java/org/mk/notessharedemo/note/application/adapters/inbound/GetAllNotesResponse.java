package org.mk.notessharedemo.note.application.adapters.inbound;

import lombok.Getter;
import org.mk.notessharedemo.note.domain.model.Note;

import java.util.Collection;

class GetAllNotesResponse {
    @Getter
    private final Collection<Note> notes;

    private GetAllNotesResponse(Collection<Note> notes) {
        this.notes = notes;
    }

    static GetAllNotesResponse of(Collection<Note> notes){
        return new GetAllNotesResponse(notes);
    }
}
