package org.mk.notessharedemo.note.application.adapters.inbound

import org.mk.notessharedemo.note.domain.model.*
import org.mk.notessharedemo.note.domain.model.getnotesowned.GetNotesOwnedUseCase
import org.mk.notessharedemo.note.domain.model.readnoteowned.ReadNoteOwnedUseCase
import org.mk.notessharedemo.note.domain.model.readnotereceived.ReadNoteReceivedUseCase
import org.mk.notessharedemo.note.domain.model.sharenoteowned.ShareNoteOwnedUseCase
import spock.lang.Specification

import java.lang.Void as Should

class NoteServiceFacadeTest extends Specification {
    static final UUID USER_ID = UUID.randomUUID()
    static final UUID NOTE_ID = UUID.randomUUID()

    ShareNoteOwnedUseCase shareNoteOwned
    GetNotesOwnedUseCase getNotesOwned
    ReadNoteOwnedUseCase readNoteOwned
    ReadNoteReceivedUseCase readNoteReceived
    NoteServiceFacade sut
    def user = UserFlyweight.of(USER_ID)

    def setup() {
        shareNoteOwned = Mock()
        getNotesOwned = Mock()
        readNoteOwned = Mock()
        readNoteReceived = Mock()
        sut = new NoteServiceFacade(shareNoteOwned, getNotesOwned, readNoteOwned, readNoteReceived)
    }

    Should "run a 'get notes owned' use case when all notes requested"() {
        when:
        sut.getAllNotes(user)

        then:
        1 * getNotesOwned.runFor({ it.user == user }) >> [mockNote()]
    }


    Should "run a 'read note owned' use case when a note by id is requested"() {
        when:
        sut.getById(NOTE_ID.toString(), user)

        then:
        1 * readNoteOwned.runFor({ it.noteId == NOTE_ID; it.user == user }) >> mockNote()

    }

    Should "run a 'share note owned' use case when note for share is posted"() {
        given:
        def ttl = 10L
        def request = new PostSharedNoteRequest(NOTE_ID, ttl, "")

        when:
        sut.share(request, user)

        then:
        1 * shareNoteOwned.runFor({ it.noteId == NOTE_ID; it.ttl == ttl }) >> mockSharedNote()
    }

    Should "run a 'read note received' use case"() {
        given:
        def pass = 'pass'

        when:
        sut.getSharedById(NOTE_ID.toString(), pass)

        then:
        1 * readNoteReceived
                .runFor({ it.noteId == NOTE_ID; it.password == new NoteAccessPassword(pass) }) >> mockNote()
    }

    private static mockNote() {
        new SimpleNote(
                new SimpleNoteData('', ''),
                new SimpleNoteMetadata(NOTE_ID, USER_ID)
        )
    }

    private static mockSharedNote(Long ttl = null) {
        new SharedNote(
                new SimpleNoteData('', ''),
                new SharedNoteMetadataImpl(NOTE_ID, USER_ID, ttl, '', '')
        )
    }

}
