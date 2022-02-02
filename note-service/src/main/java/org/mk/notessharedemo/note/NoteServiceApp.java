package org.mk.notessharedemo.note;

import lombok.extern.slf4j.Slf4j;
import org.mk.notessharedemo.note.application.adapters.outbound.NoteRedisEntity;
import org.mk.notessharedemo.note.application.ports.outbound.NoteRepository;
import org.mk.notessharedemo.note.domain.model.SimpleNote;
import org.mk.notessharedemo.note.domain.model.SimpleNoteData;
import org.mk.notessharedemo.note.domain.model.SimpleNoteMetadata;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication
public class NoteServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(NoteServiceApp.class, args);
    }

    @Bean
    @Profile("!test")
    ApplicationRunner loadDatabase(NoteRepository noteRepo) {
        return appRunner -> {
            log.debug("Initializing database..");
            Stream.of(
                            new SimpleNote(
                                    new SimpleNoteData("note #1", "just a note"),
                                    new SimpleNoteMetadata(UUID.fromString("b47cd176-40c2-4b77-8e2f-4b9d791394da"), UUID.fromString("001af529-5500-4879-952b-be21998ef35e"))
                            ),
                            new SimpleNote(
                                    new SimpleNoteData("note #2", "a short story"),
                                    new SimpleNoteMetadata(UUID.fromString("dc4ae2aa-4a7b-45ae-8b75-8916b154b3d2"), UUID.fromString("001af529-5500-4879-952b-be21998ef35e"))
                            ),
                            new SimpleNote(
                                    new SimpleNoteData("note #3", "a short story"),
                                    new SimpleNoteMetadata(UUID.fromString("ecf448b3-47f3-4ec0-924a-d8beb4391ed9"), UUID.fromString("001af529-5500-4879-952b-be21998ef35e"))
                            ),
                            new SimpleNote(
                                    new SimpleNoteData("memories", "once upon a time..."),
                                    new SimpleNoteMetadata(UUID.fromString("abcdabcd-47f3-4ec0-924a-d8beb4391234"), UUID.fromString("eedfb62a-24c0-4c33-ae69-c03b9401dd0b"))
                            )
                    )
                    .map(NoteRedisEntity::fromNote)
                    .forEach(noteRepo::save);
            log.info("Database initialised with 4 Notes");
        };
    }
}
