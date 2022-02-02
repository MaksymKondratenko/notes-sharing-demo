package org.mk.notessharedemo.note.domain.model.readnotereceived

import org.mk.notessharedemo.note.application.adapters.outbound.SharedNoteRedisEntity
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepository
import org.mk.notessharedemo.note.domain.model.*
import spock.lang.Specification

class ReadNoteReceivedImplTest extends Specification {
    private static PASS = "mockPassword"
    private static TEST_NOTE_ID = UUID.randomUUID()
    private static TEST_OWNER_ID = UUID.randomUUID()

    SharedNoteRepository repoMock
    ReadNoteReceivedImpl sut

    def setup() {
        repoMock = Mock(SharedNoteRepository)
        sut = new ReadNoteReceivedImpl(repoMock)
    }

    def "read note received use case should query a shared note by its id and return it if pass is proper"() {
        given:
        def queryWithProperPass = new ReadNoteReceivedQueryImpl(TEST_NOTE_ID, new NoteAccessPassword(PASS))
        when:
        def note = sut.runFor(queryWithProperPass)

        then:
        1 * repoMock.findById(TEST_NOTE_ID) >> Optional.of(mockSharedNote())
        note != null
    }

    def "read notes owned use case should throw an exception if note of such id is not present"() {
        given:
        def query = new ReadNoteReceivedQueryImpl(TEST_NOTE_ID, new NoteAccessPassword(PASS))

        when:
        def note = sut.runFor(query)

        then:
        1 * repoMock.findById(TEST_NOTE_ID) >> Optional.empty()
        thrown NoteDoesNotExistException
    }

    def "read notes owned use case should throw an exception if pass provided is wrong"() {
        given:
        def queryWithWrongPass = new ReadNoteReceivedQueryImpl(TEST_NOTE_ID, new NoteAccessPassword("wrongPass"))

        when:
        def note = sut.runFor(queryWithWrongPass)

        then:
        1 * repoMock.findById(TEST_NOTE_ID) >> Optional.of(mockSharedNote())
        thrown NoteAccessForbiddenException
    }

    private static mockSharedNote() {
        SharedNoteRedisEntity.builder()
                .title("")
                .content("")
                .id(UUID.randomUUID())
                .ownerId(TEST_OWNER_ID)
                .ttl(1L)
                .associatedKey(PASS)
                .build()
    }
}
