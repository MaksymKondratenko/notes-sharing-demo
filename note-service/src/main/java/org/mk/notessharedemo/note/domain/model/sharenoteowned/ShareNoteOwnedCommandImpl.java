package org.mk.notessharedemo.note.domain.model.sharenoteowned;

import lombok.Builder;
import lombok.Getter;
import org.mk.notessharedemo.note.domain.model.UserFlyweight;

import java.util.Optional;
import java.util.UUID;

@Builder
public class ShareNoteOwnedCommandImpl implements ShareNoteOwnedCommand {
    @Getter
    private final UUID noteId;
    @Getter
    private final UserFlyweight user;
    private final Long ttl;
    @Getter
    private final String recipientEmail;

    @Override
    public Long getTtlOrDefault(Long defaultTtl) {
        return Optional.ofNullable(ttl)
                .orElse(defaultTtl);
    }
}
