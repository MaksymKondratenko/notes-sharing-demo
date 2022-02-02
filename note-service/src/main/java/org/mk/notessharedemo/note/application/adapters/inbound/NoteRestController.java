package org.mk.notessharedemo.note.application.adapters.inbound;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mk.notessharedemo.note.domain.model.NoteDoesNotExistException;
import org.mk.notessharedemo.note.domain.model.UserFlyweight;
import org.mk.notessharedemo.note.domain.model.readnotereceived.NoteAccessForbiddenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
public class NoteRestController {
    static final String TOKEN_HEADER_KEY = "X-Access-Token";
    private final NoteServiceFacade noteService;
    private final AuthenticationServiceFacade authService;

    @GetMapping("/note/all")
    GetAllNotesResponse getAllNotes(@NotBlank @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        log.debug("Processing 'get all notes' request.");
        UserFlyweight user = authService.authenticate(authToken);
        return noteService.getAllNotes(user);
    }

    @GetMapping("/note/{id}")
    GetNoteByIdResponse getById(@ValidId @PathVariable("id") String noteId,
                                       @NotBlank @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        log.debug("Processing 'read note by id' request. Note id: {}.", noteId);
        UserFlyweight user = authService.authenticate(authToken);
        return noteService.getById(noteId, user);
    }

    @PostMapping("/shared-note")
    ResponseEntity<PostSharedNoteResponse> share(@Valid @RequestBody PostSharedNoteRequest request,
                                                        @NotBlank @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        log.debug("Processing 'share a note' request'. Note id: {}, ttl: {}.", request.getNoteId(), request.getTtlSeconds());
        UserFlyweight user = authService.authenticate(authToken);
        PostSharedNoteResponse response = noteService.share(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/shared-note/{id}")
    GetSharedNoteByIdResponse getSharedById(@ValidId @PathVariable("id") String noteId,
                                                   @NotBlank @RequestHeader(TOKEN_HEADER_KEY) String password) {
        log.debug("Processing 'read note received' request. Note id: {}.", noteId);
        return noteService.getSharedById(noteId, password);
    }

    @ExceptionHandler(NoteDoesNotExistException.class)
    public ResponseEntity<?> onNoteDoesNotExistException() {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoteAccessForbiddenException.class)
    public ResponseEntity<?> onNoteAccessForbiddenException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> onConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

