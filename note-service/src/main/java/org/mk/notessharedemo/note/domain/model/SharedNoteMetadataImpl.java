package org.mk.notessharedemo.note.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class SharedNoteMetadataImpl implements SharedNoteMetadata {
    private final UUID id;
    private final UUID ownerId;
    private final Long ttl;
    private final String associatedKey;
    private final String recipientEmail;

    public static SharedNoteMetadataImpl from(SharedNoteMetadata metadata) {
        return new SharedNoteMetadataImpl(metadata.getId(), metadata.getOwnerId(), metadata.getTtl(),
                metadata.getAssociatedKey(), metadata.getRecipientEmail());
    }

    @Override
    public boolean canBeAcquiredWith(NoteAccessPassword password) {
        return Optional.ofNullable(password)
                .map(NoteAccessPassword::getValue)
                .orElse("")
                .equals(associatedKey);
    }
}
