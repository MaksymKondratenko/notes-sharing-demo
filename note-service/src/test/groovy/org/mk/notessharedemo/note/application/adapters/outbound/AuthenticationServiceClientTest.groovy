package org.mk.notessharedemo.note.application.adapters.outbound

import spock.lang.Specification

import static org.mk.notessharedemo.note.application.adapters.outbound.AuthenticationServiceClient.*

class AuthenticationServiceClientTest extends Specification {

    def "user should be authenticated by password"() {
        given:
        def sut = new AuthenticationServiceClient()

        when:
        def actualId = sut.authenticate(password)

        then:
        expectedId == actualId?.get()?.toString()

        where:
        password    | expectedId
        FIRST_TOKEN | FIRST_ID
        SECOND_TOKEN | SECOND_ID
    }
}
