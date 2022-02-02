package org.mk.notessharedemo.note.application.adapters.inbound;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
class PostSharedNoteRequest {
    @NotNull
    private final UUID noteId;
    @Digits(integer = 7, fraction = 0)
    private final long ttlSeconds;
    @Email
    private final String recipientEmail;
}
