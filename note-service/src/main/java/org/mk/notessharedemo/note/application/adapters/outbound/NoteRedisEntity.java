package org.mk.notessharedemo.note.application.adapters.outbound;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.mk.notessharedemo.note.domain.model.Note;
import org.mk.notessharedemo.note.domain.model.SimpleNote;
import org.mk.notessharedemo.note.domain.model.SimpleNoteData;
import org.mk.notessharedemo.note.domain.model.SimpleNoteMetadata;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@RedisHash("Note")
@EqualsAndHashCode
@Builder(access = PRIVATE)
public class NoteRedisEntity implements Serializable {
    @Id
    private final UUID id;
    @Indexed
    private final UUID ownerId;
    private final String title;
    private final String content;

    public static NoteRedisEntity fromNote(Note note) {
        return NoteRedisEntity.builder()
                .id(note.getMetadata().getId())
                .ownerId(note.getMetadata().getOwnerId())
                .title(note.getData().getTitle())
                .content(note.getData().getContent())
                .build();
    }

    public Note toNote() {
        return new SimpleNote(new SimpleNoteData(title, content), new SimpleNoteMetadata(id, ownerId));
    }
}
