package org.mk.notessharedemo.note.application.adapters

import org.mk.notessharedemo.note.application.ports.outbound.NoteSharedNotificationService
import org.mk.notessharedemo.note.domain.model.SharedNoteMetadata
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class NotificationTestConfiguration {
    @Bean
    @Primary
    NoteSharedNotificationService notifier(){
        new NoteSharedNotificationService() {
            @Override
            void sendNotification(SharedNoteMetadata noteMetadata) {
                // this is a stub. we don't want to send any email to send
            }
        }
    }
}
