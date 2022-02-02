package org.mk.notessharedemo.note.domain.model.readnoteowned

import org.mk.notessharedemo.note.application.adapters.outbound.NoteRedisEntity
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepository
import org.mk.notessharedemo.note.domain.model.NoteDoesNotExistException
import org.mk.notessharedemo.note.domain.model.SimpleNote
import org.mk.notessharedemo.note.domain.model.SimpleNoteData
import org.mk.notessharedemo.note.domain.model.SimpleNoteMetadata
import org.mk.notessharedemo.note.domain.model.UserFlyweight
import spock.lang.Specification

class ReadNoteOwnedImplTest extends Specification {
    private static TEST_OWNER_ID = UUID.randomUUID()
    private static TEST_NOTE_ID = UUID.randomUUID()

    NoteRepository repoMock
    ReadNoteOwnedQuery query
    ReadNoteOwnedImpl sut

    def setup() {
        repoMock = Mock(NoteRepository)
        query = new ReadNoteOwnedQueryImpl(TEST_NOTE_ID, UserFlyweight.of(TEST_OWNER_ID))
        sut = new ReadNoteOwnedImpl(repoMock)
    }

    def "read notes owned use case should query a note by its id"() {
        when:
        def note = sut.runFor(query)

        then:
        1 * repoMock.findById(TEST_NOTE_ID) >> Optional.of(mockNote())
        note != null
    }

    def "read notes owned use case should throw an exception if note of such id is not present"() {
        when:
        def note = sut.runFor(query)

        then:
        1 * repoMock.findById(TEST_NOTE_ID) >> Optional.empty()
        thrown NoteDoesNotExistException
    }

    private static mockNote() {
        NoteRedisEntity.builder()
                .content("")
                .title("")
                .id(TEST_NOTE_ID)
                .ownerId(TEST_OWNER_ID)
                .build()
    }
}
