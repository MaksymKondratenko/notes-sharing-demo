package org.mk.notessharedemo.note.application.adapters.outbound

import org.mk.notessharedemo.note.application.ports.outbound.NoteRepository
import spock.lang.Specification

class NoteRepositoryFacadeImplTest extends Specification {
    static TEST_OWNER_ID = UUID.randomUUID()

    def "should delegate to note repository"() {
        given:
        def repoMock = Mock(NoteRepository)
        def sut = new NoteRepositoryFacadeImpl(repoMock)

        when:
        sut.findAllByOwnerId(TEST_OWNER_ID)

        then:
        1 * repoMock.findAllByOwnerId(TEST_OWNER_ID) >> [mockNote(), mockNote()]

    }

    private static mockNote() {
        NoteRedisEntity.builder()
                .title("")
                .content("")
                .id(UUID.randomUUID())
                .ownerId(TEST_OWNER_ID)
                .build()
    }
}
