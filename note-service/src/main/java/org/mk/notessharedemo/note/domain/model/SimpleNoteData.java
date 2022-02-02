package org.mk.notessharedemo.note.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class SimpleNoteData implements NoteData {
    private final String title;
    private final String content;

    public static SimpleNoteData from(NoteData data){
        return new SimpleNoteData(data.getTitle(), data.getContent());
    }

}
