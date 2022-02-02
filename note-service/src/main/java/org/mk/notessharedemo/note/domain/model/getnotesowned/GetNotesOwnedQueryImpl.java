package org.mk.notessharedemo.note.domain.model.getnotesowned;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mk.notessharedemo.note.domain.model.UserFlyweight;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GetNotesOwnedQueryImpl implements GetNotesOwnedQuery {
    private final UserFlyweight user;
}
