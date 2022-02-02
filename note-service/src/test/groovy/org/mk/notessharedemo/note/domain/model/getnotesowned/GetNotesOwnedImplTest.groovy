package org.mk.notessharedemo.note.domain.model.getnotesowned


import org.mk.notessharedemo.note.application.ports.outbound.NoteRepositoryFacade
import org.mk.notessharedemo.note.domain.model.SimpleNote
import org.mk.notessharedemo.note.domain.model.SimpleNoteData
import org.mk.notessharedemo.note.domain.model.SimpleNoteMetadata
import org.mk.notessharedemo.note.domain.model.UserFlyweight
import spock.lang.Specification

class GetNotesOwnedImplTest extends Specification {
    private static TEST_OWNER_ID = UUID.randomUUID()

    def "get notes owned use case should query all notes for an owner"() {
        given:
        def repoMock = Mock(NoteRepositoryFacade)
        def query = new GetNotesOwnedQueryImpl(UserFlyweight.of(TEST_OWNER_ID))
        def sut = new GetNotesOwnedImpl(repoMock)

        when:
        sut.runFor(query)

        then:
        1 * repoMock.findAllByOwnerId(TEST_OWNER_ID) >> [mockNote()]
    }

    private static mockNote() {
        new SimpleNote(
                new SimpleNoteData("", ""),
                new SimpleNoteMetadata(UUID.randomUUID(), TEST_OWNER_ID)
        )
    }
}
