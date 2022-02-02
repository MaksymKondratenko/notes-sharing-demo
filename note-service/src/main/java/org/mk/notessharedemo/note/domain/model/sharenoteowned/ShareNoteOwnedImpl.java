package org.mk.notessharedemo.note.domain.model.sharenoteowned;

import lombok.RequiredArgsConstructor;
import org.mk.notessharedemo.note.application.adapters.outbound.NoteRedisEntity;
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepository;
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepositoryFacade;
import org.mk.notessharedemo.note.domain.model.*;

@RequiredArgsConstructor
public class ShareNoteOwnedImpl implements ShareNoteOwnedUseCase {
    static final Long DEFAULT_TTL = -1L;

    private final NoteRepository noteRepo;
    private final SharedNoteRepositoryFacade sharedNoteRepo;
    private final AccessKeyGenerator keyGenerator;

    @Override
    public SharedNote runFor(ShareNoteOwnedCommand command) {
        var password = new NoteAccessPassword(keyGenerator.issueKey());
        Note noteToShare = noteRepo.findById(command.getNoteId())
                .map(NoteRedisEntity::toNote)
                .filter(note -> note.isOwnedBy(command.getUser()))
                .orElseThrow(NoteDoesNotExistException::new);
        Long ttl = command.getTtlOrDefault(DEFAULT_TTL);
        return sharedNoteRepo.save(SharedNote.fromNote(noteToShare, password, ttl, command.getRecipientEmail()));
    }
}
