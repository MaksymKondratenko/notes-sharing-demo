package org.mk.notessharedemo.note.domain.model

import spock.lang.Specification

class SharedNoteMetadataImplTest extends Specification {
    static PASS = "correct pass"
    static WRONG_PASS = "wrong pass"

    SharedContent sut

    def setup() {
        sut = new SharedNoteMetadataImpl(UUID.randomUUID(), UUID.randomUUID(), 10L, PASS, "")
    }


    def "content should be acquired when associated and provided keys are same"() {
        given:
        def password = new NoteAccessPassword(key)

        when:
        def actual = sut.canBeAcquiredWith(password)

        then:
        expected == actual

        where:
        key        | expected
        PASS       | true
        WRONG_PASS | false
        " "        | false
        ""         | false
        null       | false
    }
}
