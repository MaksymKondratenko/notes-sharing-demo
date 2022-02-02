package org.mk.notessharedemo.note.application.ports.outbound;

import org.mk.notessharedemo.note.application.adapters.outbound.SharedNoteRedisEntity;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

import java.util.UUID;

public interface SharedNoteRepository extends KeyValueRepository<SharedNoteRedisEntity, UUID> {
}
