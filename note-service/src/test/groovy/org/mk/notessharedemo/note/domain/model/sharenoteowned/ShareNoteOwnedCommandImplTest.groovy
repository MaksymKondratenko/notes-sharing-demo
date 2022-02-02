package org.mk.notessharedemo.note.domain.model.sharenoteowned

import spock.lang.Specification

class ShareNoteOwnedCommandImplTest extends Specification {
    static DEFAULT_TTL = -1L

    def "should return proper TTL"() {
        given:
        def command = ShareNoteOwnedCommandImpl.builder()
                .ttl(ttl)
                .build()

        when:
        def actual = command.getTtlOrDefault(DEFAULT_TTL)

        then:
        expected == actual

        where:
        ttl  | expected
        10L  | 10L
        300L | 300L
        0L   | 0L
        null | -1L
    }
}
