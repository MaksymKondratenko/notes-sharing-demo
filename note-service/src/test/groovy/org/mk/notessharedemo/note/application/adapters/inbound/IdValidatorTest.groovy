package org.mk.notessharedemo.note.application.adapters.inbound

import static org.mk.notessharedemo.note.application.adapters.outbound.AuthenticationServiceClient.FIRST_ID
import spock.lang.Specification


class IdValidatorTest extends Specification {

    def "should be valid only if a string ID has UUID format"() {
        given:
        def sut = new IdValidator()

        when:
        def actual = sut.isValid(id, context)

        then:
        expected == actual

        where:
        id       | context | expected
        FIRST_ID | null    | true
        'sb'     | null    | false
        ' - '    | null    | false
        '  '     | null    | false
        null     | null    | false
    }
}
