package org.mk.notessharedemo.note.application.ports.outbound;

import org.mk.notessharedemo.note.application.adapters.outbound.NoteRedisEntity;
import org.mk.notessharedemo.note.domain.model.Note;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends KeyValueRepository<NoteRedisEntity, UUID> {
     Collection<NoteRedisEntity> findAllByOwnerId(UUID ownerId);
}
