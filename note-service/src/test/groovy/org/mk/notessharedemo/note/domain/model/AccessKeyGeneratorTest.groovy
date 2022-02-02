package org.mk.notessharedemo.note.domain.model

import spock.lang.Specification

class AccessKeyGeneratorTest extends Specification {

    def "should not be deterministic"() {
        given:
        def sut = new AccessKeyGenerator()

        expect:
        sut.issueKey() != sut.issueKey()
    }
}
