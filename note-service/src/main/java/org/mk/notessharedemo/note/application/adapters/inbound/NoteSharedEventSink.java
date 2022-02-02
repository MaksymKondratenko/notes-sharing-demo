package org.mk.notessharedemo.note.application.adapters.inbound;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mk.notessharedemo.note.domain.model.notifynoteshared.NotifyNoteSharedCommandImpl;
import org.mk.notessharedemo.note.domain.model.notifynoteshared.NotifyNoteSharedUseCase;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class NoteSharedEventSink implements MessageListener {
    private final NotifyNoteSharedUseCase notifyNoteSharedUseCase;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String eventChannelName = new String(message.getChannel(), StandardCharsets.UTF_8);
        UUID sharedNoteId = UUID.fromString(eventChannelName.split(":")[2]);
        log.debug("Received NoteShared event notification. Note id: {}.", sharedNoteId);
        notifyNoteSharedUseCase.runFor(new NotifyNoteSharedCommandImpl(sharedNoteId));
    }
}
