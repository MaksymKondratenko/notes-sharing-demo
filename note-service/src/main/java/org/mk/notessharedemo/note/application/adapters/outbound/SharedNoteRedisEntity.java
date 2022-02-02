package org.mk.notessharedemo.note.application.adapters.outbound;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.mk.notessharedemo.note.domain.model.SharedNote;
import org.mk.notessharedemo.note.domain.model.SharedNoteMetadataImpl;
import org.mk.notessharedemo.note.domain.model.SimpleNoteData;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@RedisHash("SharedNote")
@EqualsAndHashCode
@Builder(access = PRIVATE)
public class SharedNoteRedisEntity implements Serializable {
    @Id
    private final UUID id;
    private final UUID ownerId;
    private final String content;
    private final String title;
    @TimeToLive
    private final Long ttl;
    private final String associatedKey;
    private final String recipientEmail;

    public static SharedNoteRedisEntity fromNote(SharedNote note) {
        return SharedNoteRedisEntity.builder()
                .id(note.getMetadata().getId())
                .ownerId(note.getMetadata().getOwnerId())
                .title(note.getData().getTitle())
                .content(note.getData().getContent())
                .ttl(note.getMetadata().getTtl())
                .associatedKey(note.getMetadata().getAssociatedKey())
                .recipientEmail(note.getMetadata().getRecipientEmail())
                .build();
    }

    public SharedNote toNote() {
        return new SharedNote(new SimpleNoteData(title, content), new SharedNoteMetadataImpl(id, ownerId, ttl, associatedKey, recipientEmail));
    }
}
