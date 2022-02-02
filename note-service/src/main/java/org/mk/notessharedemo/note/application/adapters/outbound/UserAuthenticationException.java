package org.mk.notessharedemo.note.application.adapters.outbound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserAuthenticationException extends RuntimeException {
}
