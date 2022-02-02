package org.mk.notessharedemo.note.domain.model.getnotesowned;

import lombok.RequiredArgsConstructor;
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepositoryFacade;
import org.mk.notessharedemo.note.domain.model.Note;

import java.util.Collection;

@RequiredArgsConstructor
public class GetNotesOwnedImpl implements GetNotesOwnedUseCase {
    private final NoteRepositoryFacade noteRepo;

    @Override
    public Collection<Note> runFor(GetNotesOwnedQuery query) {
        return noteRepo.findAllByOwnerId(query.getUser().getId());
    }
}
