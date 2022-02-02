package org.mk.notessharedemo.note.application.adapters

import org.mk.notessharedemo.note.application.DomainSpringConfig
import org.mk.notessharedemo.note.application.adapters.inbound.NoteRestController
import org.mk.notessharedemo.note.application.adapters.inbound.PostSharedNoteRequest
import org.mk.notessharedemo.note.application.adapters.inbound.TestAuthService
import org.mk.notessharedemo.note.application.adapters.inbound.TestNoteService
import org.mk.notessharedemo.note.application.adapters.outbound.AuthenticationServiceClient
import org.mk.notessharedemo.note.application.adapters.outbound.NoteRedisEntity
import org.mk.notessharedemo.note.application.adapters.outbound.NoteRepositoryFacadeImpl
import org.mk.notessharedemo.note.application.adapters.outbound.SharedNoteRepositoryFacadeImpl
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepository
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepository
import org.mk.notessharedemo.note.domain.model.SimpleNote
import org.mk.notessharedemo.note.domain.model.SimpleNoteData
import org.mk.notessharedemo.note.domain.model.SimpleNoteMetadata
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static org.junit.platform.commons.util.StringUtils.isNotBlank
import static org.mk.notessharedemo.note.application.adapters.outbound.AuthenticationServiceClient.*

@ActiveProfiles('test')
@SpringBootTest(classes = [
        RedisTestConfiguration,
        TestAuthService,
        TestNoteService,
        AuthenticationServiceClient,
        DomainSpringConfig,
        SharedNoteRepositoryFacadeImpl,
        NoteRepositoryFacadeImpl,
        NoteRestController,
        NotificationTestConfiguration
])
class RestApiToRedisEndToEndTest extends Specification {

    @Autowired
    NoteRestController controller

    @Autowired
    NoteRepository noteRepo
    @Autowired
    SharedNoteRepository sharedNoteRepo

    def firstNoteOfUserOne = new SimpleNote(
            new SimpleNoteData('note #1', 'just a note'),
            new SimpleNoteMetadata(UUID.fromString('b47cd176-40c2-4b77-8e2f-4b9d791394da'), UUID.fromString(FIRST_ID))
    )
    def secondNoteOfUserOne = new SimpleNote(
            new SimpleNoteData('note #2', 'a short story'),
            new SimpleNoteMetadata(UUID.fromString('dc4ae2aa-4a7b-45ae-8b75-8916b154b3d2'), UUID.fromString(FIRST_ID))
    )
    def thirdNoteOfUserOne = new SimpleNote(
            new SimpleNoteData('note #3', 'a short story'),
            new SimpleNoteMetadata(UUID.fromString('ecf448b3-47f3-4ec0-924a-d8beb4391ed9'), UUID.fromString(FIRST_ID))
    )
    def noteOfUserTwo = new SimpleNote(
            new SimpleNoteData('memories', 'once upon a time...'),
            new SimpleNoteMetadata(UUID.fromString('abcdabcd-47f3-4ec0-924a-d8beb4391234'), UUID.fromString(SECOND_ID))
    )

    def setup() {
        [
                NoteRedisEntity.fromNote(firstNoteOfUserOne),
                NoteRedisEntity.fromNote(secondNoteOfUserOne),
                NoteRedisEntity.fromNote(thirdNoteOfUserOne),
                NoteRedisEntity.fromNote(noteOfUserTwo),
        ].forEach({ it -> noteRepo.save(it) })
    }

    def cleanup() {
        [
                NoteRedisEntity.fromNote(firstNoteOfUserOne),
                NoteRedisEntity.fromNote(secondNoteOfUserOne),
                NoteRedisEntity.fromNote(thirdNoteOfUserOne),
                NoteRedisEntity.fromNote(noteOfUserTwo),
        ].forEach({ it -> noteRepo.delete(it) })
    }

    // GET /note/all
    def "should respond with all notes to get all notes request when a user is authenticated"() {
        given:
        def expectedNotes = [firstNoteOfUserOne, secondNoteOfUserOne, thirdNoteOfUserOne]

        when:
        def actualNotesResponse = controller.getAllNotes FIRST_TOKEN

        then:
        actualNotesResponse?.notes?.size() == 3
        actualNotesResponse?.notes?.forEach({
            it -> expectedNotes.contains(it)
        })
        !actualNotesResponse?.notes?.contains(noteOfUserTwo)

    }

    // GET /note/{id}
    def "should respond with a proper note to get note by id request when a user is authenticated"() {
        given:
        def expectedNote = firstNoteOfUserOne
        def noteId = firstNoteOfUserOne.metadata.id.toString()

        when:
        def actualNoteByIdResponse = controller.getById(noteId, FIRST_TOKEN)

        then:
        actualNoteByIdResponse?.note == expectedNote
    }

    // POST /shared-note
    def "should share a proper note on share note request when a user is authenticated"() {
        given:
        def noteId = firstNoteOfUserOne.metadata.id
        def postShareNoteRequest = new PostSharedNoteRequest(noteId, 120L, "")

        when:
        def actualResponse = controller.share(postShareNoteRequest, FIRST_TOKEN)

        then:
        isNotBlank actualResponse?.body?.password
        actualResponse?.body?.sharedNoteId == noteId
    }

    // GET /shared-note/{id}
    def "should return a proper note content to get shared note by id request when a password is proper"() {
        given:
        def noteId = secondNoteOfUserOne.metadata.id
        def postShareNoteRequest = new PostSharedNoteRequest(noteId, 120L, "")
        def sharedNote = controller.share(postShareNoteRequest, FIRST_TOKEN)
        def password = sharedNote?.body?.password

        when:
        def actualResponse = controller.getSharedById(noteId.toString(), password)

        then:
        actualResponse?.note?.title == secondNoteOfUserOne.data.title
        actualResponse?.note?.content == secondNoteOfUserOne.data.content
    }
}
