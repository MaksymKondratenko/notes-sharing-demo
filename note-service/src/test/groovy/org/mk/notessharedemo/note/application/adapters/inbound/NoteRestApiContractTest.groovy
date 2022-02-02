package org.mk.notessharedemo.note.application.adapters.inbound

import org.mk.notessharedemo.note.application.DomainSpringConfig
import org.mk.notessharedemo.note.application.adapters.NotificationTestConfiguration
import org.mk.notessharedemo.note.application.adapters.outbound.*
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepository
import org.mk.notessharedemo.note.application.ports.outbound.SharedNoteRepository
import org.mk.notessharedemo.note.domain.model.*
import org.mk.notessharedemo.note.domain.model.getnotesowned.GetNotesOwnedUseCase
import org.mk.notessharedemo.note.domain.model.readnoteowned.ReadNoteOwnedUseCase
import org.mk.notessharedemo.note.domain.model.readnotereceived.NoteAccessForbiddenException
import org.mk.notessharedemo.note.domain.model.readnotereceived.ReadNoteReceivedUseCase
import org.mk.notessharedemo.note.domain.model.sharenoteowned.ShareNoteOwnedUseCase
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.keyvalue.core.KeyValueOperations
import org.springframework.data.keyvalue.repository.support.SimpleKeyValueRepository
import org.springframework.data.repository.core.EntityInformation
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.mk.notessharedemo.note.application.adapters.inbound.NoteRestController.TOKEN_HEADER_KEY
import static NoteRestApiContractTest.TEST_NOTE_ID
import static NoteRestApiContractTest.TEST_OWNER_ID
import static org.mk.notessharedemo.note.application.adapters.outbound.AuthenticationServiceClient.FIRST_TOKEN
import static org.springframework.http.HttpHeaders.AUTHORIZATION
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
@ActiveProfiles('test')
@Import([TestAuthService.class,
        TestNoteService.class,
        DomainSpringConfig.class,
        SharedNoteRepositoryFacadeImpl.class,
        NoteRepositoryFacadeImpl.class,
        NotificationTestConfiguration.class
])
class NoteRestApiContractTest extends Specification {
    static TEST_NOTE_ID = UUID.randomUUID()
    static TEST_OWNER_ID = UUID.randomUUID()
    public static final String TEST_PASS = 'pass'

    @Autowired
    MockMvc mockMvc

    @SpringBean
    TestNoteService noteServiceMock = Mock()
    @SpringBean
    TestAuthService authServiceMock = Mock()

    // GET /note/all
    def "should respond with all notes to get all notes request when a user is authenticated"() {
        given:
        def user = UserFlyweight.of(TEST_OWNER_ID)
        authServiceMock.authenticate(_ as String) >> user
        noteServiceMock.getAllNotes(user) >> GetAllNotesResponse.of([mockNote()])

        expect:
        mockMvc.perform(get('/note/all').header(AUTHORIZATION, FIRST_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$').exists())
                .andExpect(jsonPath('$.notes').exists())
                .andExpect(jsonPath('$.notes').isArray())
    }

    def "should respond with 401 to get all notes request when a user is not authenticated"() {
        given:
        authServiceMock.authenticate(_ as String) >> { throw new UserAuthenticationException() }

        expect:
        mockMvc.perform(get('/note/all').header(AUTHORIZATION, FIRST_TOKEN))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath('$').doesNotExist())
    }

    def "should respond with 400 to get all notes request when auth token is not valid"() {
        expect:
        mockMvc.perform(get('/note/all').header(AUTHORIZATION, token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.notes').doesNotExist())

        where:
        token | _
        '  '  | _
        ''    | _
    }

    def "should respond with 400 to get all notes request when auth token is not present"() {
        expect:
        mockMvc.perform(get('/note/all'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.notes').doesNotExist())
    }

    // GET /note/{id}
    def "should respond with a note to get note by id request when a user is authenticated"() {
        given:
        def user = UserFlyweight.of(TEST_OWNER_ID)
        authServiceMock.authenticate(_ as String) >> user
        noteServiceMock.getById(TEST_NOTE_ID.toString(), user) >> GetNoteByIdResponse.of(mockNote())

        expect:
        mockMvc.perform(get("/note/$TEST_NOTE_ID").header(AUTHORIZATION, FIRST_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$').exists())
                .andExpect(jsonPath('$.note').exists())
    }

    def "should respond with no content to get note by id request when there is no note with such ID"() {
        given:
        def user = UserFlyweight.of(TEST_OWNER_ID)
        authServiceMock.authenticate(_ as String) >> user
        noteServiceMock.getById(_ as String, user) >> { throw new NoteDoesNotExistException() }

        expect:
        mockMvc.perform(get("/note/$TEST_NOTE_ID".replace('a', 'b')).header(AUTHORIZATION, FIRST_TOKEN))
                .andExpect(status().isNoContent())
    }


    def "should respond with 401 to get note by id request when a user is not authenticated"() {
        given:
        authServiceMock.authenticate(_ as String) >> { throw new UserAuthenticationException() }

        expect:
        mockMvc.perform(get("/note/$TEST_NOTE_ID").header(AUTHORIZATION, FIRST_TOKEN))
                .andExpect(status().isUnauthorized())
    }

    def "should respond with 400 to get note by id request when auth token is not valid"() {
        expect:
        mockMvc.perform(get("/note/$TEST_NOTE_ID").header(AUTHORIZATION, token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.note').doesNotExist())

        where:
        token | _
        '  '  | _
        ''    | _
    }

    def "should respond with 400 to get note by id request when auth token is not present"() {
        expect:
        mockMvc.perform(get("/note/$TEST_NOTE_ID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.note').doesNotExist())
    }

    def "should respond with 400 to get note by id request when note ID is not valid"() {
        expect:
        mockMvc.perform(get("/note/$id").header(AUTHORIZATION, FIRST_TOKEN))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.note').doesNotExist())

        where:
        id    | _
        'sb'  | _
        ' - ' | _
        '  '  | _
    }

    // POST /shared-note
    def "should respond with a response about a shared note to post shared note request when a user is authenticated"() {
        given:
        def user = UserFlyweight.of(TEST_OWNER_ID)
        authServiceMock.authenticate(_ as String) >> user
        noteServiceMock.share(_ as PostSharedNoteRequest, user) >> PostSharedNoteResponse.of(mockSharedNote())

        expect:
        mockMvc.perform(
                post('/shared-note')
                        .header(AUTHORIZATION, FIRST_TOKEN)
                        .content('{"noteId":"' + TEST_NOTE_ID + '","ttlSeconds":10}')
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath('$').exists())
                .andExpect(jsonPath('$.sharedNoteId').exists())
                .andExpect(jsonPath('$.password').exists())
    }

    def "should respond with 401 to post shared note request when a user is not authenticated"() {
        given:
        authServiceMock.authenticate(_ as String) >> { throw new UserAuthenticationException() }

        expect:
        mockMvc.perform(
                post('/shared-note')
                        .header(AUTHORIZATION, FIRST_TOKEN)
                        .content('{"noteId":"' + TEST_NOTE_ID + '","ttlSeconds":10}')
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath('$').doesNotExist())
    }

    def "should respond with 400 to post shared note request when auth token is not valid"() {
        expect:
        mockMvc.perform(post("/shared-note").header(AUTHORIZATION, token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.sharedNoteId').doesNotExist())
                .andExpect(jsonPath('$.password').doesNotExist())

        where:
        token | _
        '  '  | _
        ''    | _
    }

    def "should respond with 400 to post shared note request when auth token is not present"() {
        expect:
        mockMvc.perform(post("/shared-note"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.note').doesNotExist())
    }

    def "should respond with 400 to post shared note request when note ID is not valid"() {
        expect:
        mockMvc.perform(
                post('/shared-note')
                        .header(AUTHORIZATION, FIRST_TOKEN)
                        .content('{"noteId":"' + id + '","ttlSeconds":10}')
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.sharedNoteId').doesNotExist())
                .andExpect(jsonPath('$.password').doesNotExist())

        where:
        id    | _
        'sb'  | _
        ' - ' | _
        '  '  | _
    }

    @Unroll
    def "should respond with 400 to post shared note request when TTL value is not valid"() {
        expect:
        mockMvc.perform(
                post('/shared-note')
                        .header(AUTHORIZATION, FIRST_TOKEN)
                        .content('{"noteId":"' + TEST_NOTE_ID + '","ttlSeconds":' + ttl + '}')
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.sharedNoteId').doesNotExist())
                .andExpect(jsonPath('$.password').doesNotExist())

        where:
        ttl           | _
        1000000000000 | _
        ' '           | _
        'sba'         | _
    }

    @Unroll
    def "should respond with 400 to post shared note request when recipient email is not valid"() {
        expect:
        mockMvc.perform(
                post('/shared-note')
                        .header(AUTHORIZATION, FIRST_TOKEN)
                        .content('{"noteId":"' + TEST_NOTE_ID + '","ttlSeconds":10L, "recipientEmail":' + email + '}')
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.sharedNoteId').doesNotExist())
                .andExpect(jsonPath('$.password').doesNotExist())

        where:
        email       | _
        '  '        | _
        ' '         | _
        ''          | _
        'text'      | _
        'text@text' | _
    }


    // GET /shared-note/{id}
    def "should respond with a shared note to get shared note by id request when a password is correct"() {
        given:
        noteServiceMock.getSharedById(TEST_NOTE_ID.toString(), TEST_PASS) >> GetSharedNoteByIdResponse.of(mockSharedNote())

        expect:
        mockMvc.perform(get("/shared-note/$TEST_NOTE_ID").header(TOKEN_HEADER_KEY, TEST_PASS))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$').exists())
                .andExpect(jsonPath('$.note').exists())
    }

    def "should respond with no content to get shared note by id request when there is no note with such ID"() {
        given:
        noteServiceMock.getSharedById(TEST_NOTE_ID.toString(), _ as String) >> { throw new NoteDoesNotExistException() }

        expect:
        mockMvc.perform(get("/shared-note/$TEST_NOTE_ID").header(TOKEN_HEADER_KEY, TEST_PASS))
                .andExpect(status().isNoContent())
    }


    def "should respond with 401 to get shared note by id request when a password is wrong"() {
        given:
        def wrongPass = 'wrongpass'
        noteServiceMock.getSharedById(TEST_NOTE_ID.toString(), wrongPass) >> { throw new NoteAccessForbiddenException() }

        expect:
        mockMvc.perform(get("/shared-note/$TEST_NOTE_ID").header(TOKEN_HEADER_KEY, wrongPass))
                .andExpect(status().isForbidden())
    }

    def "should respond with 400 to get shared note by id request when password is not valid"() {
        expect:
        mockMvc.perform(get("/shared-note/$TEST_NOTE_ID").header(TOKEN_HEADER_KEY, token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.note').doesNotExist())

        where:
        token | _
        '  '  | _
        ''    | _
    }

    def "should respond with 400 to get shared note by id request when password is not present"() {
        expect:
        mockMvc.perform(get("/note/$TEST_NOTE_ID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.note').doesNotExist())
    }

    @Unroll
    def "should respond with 400 to get shared note by id request when note ID is not valid"() {
        expect:
        mockMvc.perform(get("/shared-note/$id").header(TOKEN_HEADER_KEY, FIRST_TOKEN))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.note').doesNotExist())

        where:
        id    | _
        'sb'  | _
        ' - ' | _
        '  '  | _
    }

    static Note mockNote() {
        new SimpleNote(
                new SimpleNoteData("", ""),
                new SimpleNoteMetadata(TEST_NOTE_ID, TEST_OWNER_ID)
        )
    }

    static mockSharedNote() {
        new SharedNote(
                new SimpleNoteData("", ""),
                new SharedNoteMetadataImpl(TEST_NOTE_ID, TEST_OWNER_ID, 10L, TEST_PASS, "email")
        )
    }
}

@Component
@Primary
@Profile('test')
class TestAuthService extends AuthenticationServiceFacade {
    TestAuthService(AuthenticationServiceClient authService) {
        super(authService)
    }

    @Override
    UserFlyweight authenticate(String authToken) {
        return super.authenticate(authToken)
    }
}

@Component
@Primary
@Profile('test')
class TestNoteService extends NoteServiceFacade {

    TestNoteService(ShareNoteOwnedUseCase shareNoteOwnedUseCase, GetNotesOwnedUseCase getNotesOwnedUseCase, ReadNoteOwnedUseCase readNoteOwnedUseCase, ReadNoteReceivedUseCase readNoteReceivedUseCase) {
        super(shareNoteOwnedUseCase, getNotesOwnedUseCase, readNoteOwnedUseCase, readNoteReceivedUseCase)
    }

    @Override
    GetAllNotesResponse getAllNotes(UserFlyweight user) {
        return super.getAllNotes(user)
    }

    @Override
    GetNoteByIdResponse getById(String noteId, UserFlyweight user) {
        return super.getById(noteId, user)
    }

    @Override
    PostSharedNoteResponse share(PostSharedNoteRequest request, UserFlyweight user) {
        return super.share(request, user)
    }

    @Override
    GetSharedNoteByIdResponse getSharedById(String noteId, String password) {
        return super.getSharedById(noteId, password)
    }
}
//
//@Component
//@Primary
//@Profile('test')
//class TestNoteRepo extends SimpleKeyValueRepository<NoteRedisEntity, UUID> implements NoteRepository {
//
//    TestNoteRepo(EntityInformation<NoteRedisEntity, UUID> metadata, KeyValueOperations operations) {
//        super(metadata, operations)
//    }
//
//    @Override
//    Collection<Note> findAllByOwnerId(UUID ownerId) {
//        TEST_OWNER_ID == ownerId ? [mockNote()] : []
//    }
//
//    @Override
//    Optional<Note> findByIdAndOwnerId(UUID id, UUID ownerId) {
//        TEST_NOTE_ID == id && TEST_OWNER_ID == ownerId ? Optional.of(mockNote()) : Optional.<Note> empty()
//    }
//
//    static Note mockNote() {
//        new SimpleNote(
//                new SimpleNoteData("", ""),
//                new SimpleNoteMetadata(TEST_NOTE_ID, TEST_OWNER_ID)
//        )
//    }
//
//}
//
//@Component
//@Primary
//@Profile('test')
//class TestSharedNoteRepo extends SimpleKeyValueRepository<SharedNoteRedisEntity, UUID> implements SharedNoteRepository {
//
//    TestSharedNoteRepo(EntityInformation<SharedNoteRedisEntity, UUID> metadata, KeyValueOperations operations) {
//        super(metadata, operations)
//    }
//
//    @Override
//    Optional<SharedNote> findById(UUID id) {
//        TEST_NOTE_ID == id ? Optional.of(mockSharedNote()) : Optional.<SharedNote> empty()
//    }
//
//    @Override
//    SharedNoteRedisEntity save(SharedNoteRedisEntity entity) {
//        entity
//    }
//
//    static mockSharedNote() {
//        new SharedNote(
//                new SimpleNoteData("", ""),
//                new SharedNoteMetadataImpl(TEST_NOTE_ID, TEST_OWNER_ID, 10L, 'pass')
//        )
//    }
//}