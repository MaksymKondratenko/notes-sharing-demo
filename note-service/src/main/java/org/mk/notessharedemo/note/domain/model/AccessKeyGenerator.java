package org.mk.notessharedemo.note.domain.model;

import java.util.UUID;

public class AccessKeyGenerator {
    public String issueKey(){
        return UUID.randomUUID().toString();
    }
}
