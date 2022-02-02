package org.mk.notessharedemo.note.domain.model.notifynoteshared;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mk.notessharedemo.note.application.ports.outbound.NoteSharedNotificationService;
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepository;
import org.mk.notessharedemo.note.domain.model.NoteDoesNotExistException;
import org.mk.notessharedemo.note.domain.model.SharedNote;

@RequiredArgsConstructor
public class NotifyNoteSharedImpl implements NotifyNoteSharedUseCase{
    private final SharedNoteRepository sharedNoteRepo;
    private final NoteSharedNotificationService notifier;

    @Override
    public void runFor(NotifyNoteSharedCommand command) {
        SharedNote sharedNote = sharedNoteRepo.findById(command.getNoteId())
                .orElseThrow(NoteDoesNotExistException::new)
                .toNote();
        notifier.sendNotification(sharedNote.getMetadata());
    }
}
