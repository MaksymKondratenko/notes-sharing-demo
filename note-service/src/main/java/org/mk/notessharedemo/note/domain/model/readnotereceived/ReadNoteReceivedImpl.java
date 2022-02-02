package org.mk.notessharedemo.note.domain.model.readnotereceived;

import lombok.RequiredArgsConstructor;
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepository;
import org.mk.notessharedemo.note.domain.model.Note;
import org.mk.notessharedemo.note.domain.model.NoteDoesNotExistException;
import org.mk.notessharedemo.note.domain.model.SharedNote;

@RequiredArgsConstructor
public class ReadNoteReceivedImpl implements ReadNoteReceivedUseCase {
    private final SharedNoteRepository sharedNoteRepo;

    @Override
    public Note runFor(ReadNoteReceivedQuery command) {
        SharedNote sharedNote = sharedNoteRepo.findById(command.getNoteId())
                .map(note -> note.toNote())
                .orElseThrow(NoteDoesNotExistException::new);
        if (sharedNote.getMetadata().canBeAcquiredWith(command.getPassword())) {
            return sharedNote;
        } else {
            throw new NoteAccessForbiddenException();
        }
    }
}
