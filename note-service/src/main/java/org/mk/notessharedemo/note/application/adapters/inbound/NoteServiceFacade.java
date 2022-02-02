package org.mk.notessharedemo.note.application.adapters.inbound;

import lombok.RequiredArgsConstructor;
import org.mk.notessharedemo.note.domain.model.Note;
import org.mk.notessharedemo.note.domain.model.NoteAccessPassword;
import org.mk.notessharedemo.note.domain.model.SharedNote;
import org.mk.notessharedemo.note.domain.model.UserFlyweight;
import org.mk.notessharedemo.note.domain.model.getnotesowned.GetNotesOwnedQueryImpl;
import org.mk.notessharedemo.note.domain.model.getnotesowned.GetNotesOwnedUseCase;
import org.mk.notessharedemo.note.domain.model.readnoteowned.ReadNoteOwnedQueryImpl;
import org.mk.notessharedemo.note.domain.model.readnoteowned.ReadNoteOwnedUseCase;
import org.mk.notessharedemo.note.domain.model.readnotereceived.ReadNoteReceivedQueryImpl;
import org.mk.notessharedemo.note.domain.model.readnotereceived.ReadNoteReceivedUseCase;
import org.mk.notessharedemo.note.domain.model.sharenoteowned.ShareNoteOwnedCommandImpl;
import org.mk.notessharedemo.note.domain.model.sharenoteowned.ShareNoteOwnedUseCase;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class NoteServiceFacade {
    private final ShareNoteOwnedUseCase shareNoteOwnedUseCase;
    private final GetNotesOwnedUseCase getNotesOwnedUseCase;
    private final ReadNoteOwnedUseCase readNoteOwnedUseCase;
    private final ReadNoteReceivedUseCase readNoteReceivedUseCase;

    GetAllNotesResponse getAllNotes(UserFlyweight user) {
        Collection<Note> notes = getNotesOwnedUseCase.runFor(new GetNotesOwnedQueryImpl(user));
        return GetAllNotesResponse.of(notes);
    }

    GetNoteByIdResponse getById(String noteId, UserFlyweight user) {
        var query = ReadNoteOwnedQueryImpl.builder()
                .noteId(UUID.fromString(noteId))
                .user(user)
                .build();
        Note note = readNoteOwnedUseCase.runFor(query);
        return GetNoteByIdResponse.of(note);
    }

    PostSharedNoteResponse share(PostSharedNoteRequest request, UserFlyweight user) {
        var command = ShareNoteOwnedCommandImpl.builder()
                .noteId(request.getNoteId())
                .ttl(request.getTtlSeconds())
                .user(user)
                .recipientEmail(request.getRecipientEmail())
                .build();
        SharedNote sharedNote = shareNoteOwnedUseCase.runFor(command);
        return PostSharedNoteResponse.of(sharedNote);
    }

    GetSharedNoteByIdResponse getSharedById(String noteId, String password) {
        var query = ReadNoteReceivedQueryImpl.builder()
                .noteId(UUID.fromString(noteId))
                .password(new NoteAccessPassword(password))
                .build();
        Note note = readNoteReceivedUseCase.runFor(query);
        return GetSharedNoteByIdResponse.of(note);
    }
}
