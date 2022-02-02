package org.mk.notessharedemo.note.domain.model;

public interface SharedContent {
    Long getTtl();
    String getAssociatedKey();
    String getRecipientEmail();
    boolean canBeAcquiredWith(NoteAccessPassword password);
}
