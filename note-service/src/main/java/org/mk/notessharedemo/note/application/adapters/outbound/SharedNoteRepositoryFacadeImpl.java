package org.mk.notessharedemo.note.application.adapters.outbound;

import lombok.RequiredArgsConstructor;
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepository;
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepositoryFacade;
import org.mk.notessharedemo.note.domain.model.SharedNote;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SharedNoteRepositoryFacadeImpl implements SharedNoteRepositoryFacade {
    private final SharedNoteRepository repo;
    @Override
    public SharedNote save(SharedNote note) {
        return repo.save(SharedNoteRedisEntity.fromNote(note))
                .toNote();
    }
}
