package org.mk.notessharedemo.note.domain.model.notifynoteshared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class NotifyNoteSharedCommandImpl implements NotifyNoteSharedCommand {
    private final UUID noteId;
}
