package org.mk.notessharedemo.note.application;

import org.mk.notessharedemo.note.application.ports.outbound.*;
import org.mk.notessharedemo.note.domain.model.AccessKeyGenerator;
import org.mk.notessharedemo.note.domain.model.getnotesowned.GetNotesOwnedImpl;
import org.mk.notessharedemo.note.domain.model.getnotesowned.GetNotesOwnedUseCase;
import org.mk.notessharedemo.note.domain.model.notifynoteshared.NotifyNoteSharedImpl;
import org.mk.notessharedemo.note.domain.model.notifynoteshared.NotifyNoteSharedUseCase;
import org.mk.notessharedemo.note.domain.model.readnoteowned.ReadNoteOwnedImpl;
import org.mk.notessharedemo.note.domain.model.readnoteowned.ReadNoteOwnedUseCase;
import org.mk.notessharedemo.note.domain.model.readnotereceived.ReadNoteReceivedImpl;
import org.mk.notessharedemo.note.domain.model.readnotereceived.ReadNoteReceivedUseCase;
import org.mk.notessharedemo.note.domain.model.sharenoteowned.ShareNoteOwnedImpl;
import org.mk.notessharedemo.note.domain.model.sharenoteowned.ShareNoteOwnedUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RedisConfig.class)
public class DomainSpringConfig {

    @Bean
    public AccessKeyGenerator accessKeyGenerator() {
        return new AccessKeyGenerator();
    }

    @Bean
    public GetNotesOwnedUseCase getNotesOwnedUseCase(NoteRepositoryFacade noteRepository) {
        return new GetNotesOwnedImpl(noteRepository);
    }

    @Bean
    public ReadNoteOwnedUseCase readNoteOwnedUseCase(NoteRepository noteRepository) {
        return new ReadNoteOwnedImpl(noteRepository);
    }

    @Bean
    public ShareNoteOwnedUseCase shareNoteOwnedUseCase(NoteRepository noteRepository,
                                                       SharedNoteRepositoryFacade sharedNoteRepository,
                                                       AccessKeyGenerator accessKeyGenerator) {
        return new ShareNoteOwnedImpl(noteRepository, sharedNoteRepository, accessKeyGenerator);
    }

    @Bean
    public ReadNoteReceivedUseCase readNoteReceivedUseCase(SharedNoteRepository sharedNoteRepository) {
        return new ReadNoteReceivedImpl(sharedNoteRepository);
    }

    @Bean
    public NotifyNoteSharedUseCase notifyNoteSharedUseCase(SharedNoteRepository sharedNoteRepository,
                                                         NoteSharedNotificationService notifier){
        return new NotifyNoteSharedImpl(sharedNoteRepository, notifier);
    }
}
