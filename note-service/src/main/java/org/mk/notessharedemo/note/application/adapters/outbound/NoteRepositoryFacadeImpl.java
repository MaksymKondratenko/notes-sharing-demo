package org.mk.notessharedemo.note.application.adapters.outbound;

import lombok.RequiredArgsConstructor;
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepository;
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepositoryFacade;
import org.mk.notessharedemo.note.domain.model.Note;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NoteRepositoryFacadeImpl implements NoteRepositoryFacade {
    private final NoteRepository repo;

    @Override
    public Collection<Note> findAllByOwnerId(UUID ownerId) {
        Collection<NoteRedisEntity> ownedNoteEntities = repo.findAllByOwnerId(ownerId);
        return ownedNoteEntities.stream()
                .map(NoteRedisEntity::toNote)
                .collect(Collectors.toList());
    }
}
