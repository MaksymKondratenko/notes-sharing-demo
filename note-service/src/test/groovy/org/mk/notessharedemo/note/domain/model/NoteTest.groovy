package org.mk.notessharedemo.note.domain.model

import spock.lang.Specification

class NoteTest extends Specification {
    static TEST_USER_ID = UUID.randomUUID()
    static ANOTHER_USER_ID = UUID.randomUUID()

    Note sut

    def setup() {
        sut = new SimpleNote(new SimpleNoteData("", ""), new SimpleNoteMetadata(UUID.randomUUID(), TEST_USER_ID))
    }


    def "note should respond that is owned by a user, if it has same ID as an owner"() {
        given:
        def user = UserFlyweight.of(userId)

        when:
        def actual = sut.isOwnedBy(user)

        then:
        expected == actual

        where:
        userId            | expected
        TEST_USER_ID      | true
        ANOTHER_USER_ID   | false
        UUID.randomUUID() | false
        null              | false
    }
}
