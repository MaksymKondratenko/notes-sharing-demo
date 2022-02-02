package org.mk.notessharedemo.note.application.adapters.outbound


import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepository
import org.mk.notessharedemo.note.domain.model.SharedNote
import org.mk.notessharedemo.note.domain.model.SharedNoteMetadataImpl
import org.mk.notessharedemo.note.domain.model.SimpleNoteData
import spock.lang.Specification

class SharedNoteRepositoryFacadeImplTest extends Specification {
    static TEST_OWNER_ID = UUID.randomUUID()

    def "should delegate to shared note repository"() {
        given:
        def repoMock = Mock(SharedNoteRepository)
        def sut = new SharedNoteRepositoryFacadeImpl(repoMock)
        def mockSharedNote = mockSharedNote()

        when:
        sut.save(mockSharedNote.toNote())

        then:
        1 * repoMock.save(_ as SharedNoteRedisEntity) >> mockSharedNote

    }

    private static mockSharedNote() {
        SharedNoteRedisEntity.builder()
                .title("")
                .content("")
                .id(UUID.randomUUID())
                .ownerId(TEST_OWNER_ID)
                .ttl(1L)
                .associatedKey("")
                .build()
    }
}
