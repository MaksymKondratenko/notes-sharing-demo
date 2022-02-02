package org.mk.notessharedemo.note.domain.model.notifynoteshared

import org.mk.notessharedemo.note.application.adapters.outbound.SharedNoteRedisEntity
import org.mk.notessharedemo.note.application.ports.outbound.NoteSharedNotificationService
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepository
import org.mk.notessharedemo.note.domain.model.NoteDoesNotExistException
import org.mk.notessharedemo.note.domain.model.SharedNote
import org.mk.notessharedemo.note.domain.model.SharedNoteMetadataImpl
import org.mk.notessharedemo.note.domain.model.SimpleNoteData
import spock.lang.Specification

class NotifyNoteSharedImplTest extends Specification {
    private static TEST_NOTE_ID = UUID.randomUUID()
    private static TEST_OWNER_ID = UUID.randomUUID()

    SharedNoteRepository noteRepoMock
    NoteSharedNotificationService notifierMock
    NotifyNoteSharedImpl sut

    def setup() {
        noteRepoMock = Mock(SharedNoteRepository)
        notifierMock = Mock(NoteSharedNotificationService)
        sut = new NotifyNoteSharedImpl(noteRepoMock, notifierMock)
    }

    def "should notify about a shared note"(){
        when:
        sut.runFor(mockCommand())

        then:
        1 * noteRepoMock.findById(TEST_NOTE_ID) >> Optional.of(SharedNoteRedisEntity.fromNote(mockSharedNote()))
        1 * notifierMock.sendNotification(mockSharedNote().metadata)
    }

    def "share note owned use case should throw an exception if note of such id is not present"() {
        when:
        sut.runFor(mockCommand())

        then:
        1 * noteRepoMock.findById(TEST_NOTE_ID) >> Optional.empty()
        thrown NoteDoesNotExistException
    }

    private static mockCommand() {
        new NotifyNoteSharedCommandImpl(TEST_NOTE_ID)
    }


    private static mockSharedNote() {
        new SharedNote(
                new SimpleNoteData("", ""),
                new SharedNoteMetadataImpl(TEST_NOTE_ID, TEST_OWNER_ID, 10L, "", "")
        )
    }

}
