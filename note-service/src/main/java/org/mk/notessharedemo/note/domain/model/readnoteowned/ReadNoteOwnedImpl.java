package org.mk.notessharedemo.note.domain.model.readnoteowned;

import lombok.RequiredArgsConstructor;
import org.mk.notessharedemo.note.application.adapters.outbound.NoteRedisEntity;
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepository;
import org.mk.notessharedemo.note.domain.model.Note;
import org.mk.notessharedemo.note.domain.model.NoteDoesNotExistException;

@RequiredArgsConstructor
public class ReadNoteOwnedImpl implements ReadNoteOwnedUseCase {
    private final NoteRepository noteRepo;

    @Override
    public Note runFor(ReadNoteOwnedQuery command) {
        return noteRepo.findById(command.getNoteId())
                .map(NoteRedisEntity::toNote)
                .filter(note -> note.isOwnedBy(command.getUser()))
                .orElseThrow(NoteDoesNotExistException::new);
    }
}
