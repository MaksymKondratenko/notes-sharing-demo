package org.mk.notessharedemo.note.domain.model.sharenoteowned

import org.mk.notessharedemo.note.application.adapters.outbound.NoteRedisEntity
import org.mk.notessharedemo.note.application.adapters.outbound.SharedNoteRedisEntity
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepository
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepositoryFacade
import org.mk.notessharedemo.note.domain.model.*
import spock.lang.Specification

class ShareNoteOwnedImplTest extends Specification {
    private static PASS = "mockPassword"
    private static TEST_NOTE_ID = UUID.randomUUID()
    private static TEST_OWNER_ID = UUID.randomUUID()

    NoteRepository noteRepoMock
    SharedNoteRepositoryFacade sharedNoteRepoMock
    AccessKeyGenerator keyGenMock
    ShareNoteOwnedImpl sut

    def setup() {
        noteRepoMock = Mock(NoteRepository)
        sharedNoteRepoMock = Mock(SharedNoteRepositoryFacade)
        keyGenMock = Mock(AccessKeyGenerator)
        sut = new ShareNoteOwnedImpl(noteRepoMock, sharedNoteRepoMock, keyGenMock)
    }

    def "share note owned use case should query a note by its id and save a shared note"() {
        when:
        def sharedNote = sut.runFor(shareNoteOwnedCommand())

        then:
        1 * noteRepoMock.findById(TEST_NOTE_ID) >> Optional.of(mockNote())
        1 * sharedNoteRepoMock.save(_ as SharedNote) >> mockSharedNote()
        sharedNote != null
    }

    def "share note owned use case should throw an exception if note of such id is not present"() {
        when:
        sut.runFor(shareNoteOwnedCommand())

        then:
        1 * noteRepoMock.findById(TEST_NOTE_ID) >> Optional.empty()
        thrown NoteDoesNotExistException
    }

    private static ShareNoteOwnedCommandImpl shareNoteOwnedCommand() {
        ShareNoteOwnedCommandImpl.builder()
                .noteId(TEST_NOTE_ID)
                .ttl(10L)
                .user(UserFlyweight.of(TEST_OWNER_ID))
                .build()
    }

    private static mockNote() {
        NoteRedisEntity.builder()
                .title("")
                .content("")
                .id(TEST_NOTE_ID)
                .ownerId(TEST_OWNER_ID)
                .build()
    }

    private static mockSharedNote() {
        new SharedNote(
                new SimpleNoteData("", ""),
                new SharedNoteMetadataImpl(TEST_NOTE_ID, TEST_OWNER_ID, 10L, PASS, "")
        )
    }
}
