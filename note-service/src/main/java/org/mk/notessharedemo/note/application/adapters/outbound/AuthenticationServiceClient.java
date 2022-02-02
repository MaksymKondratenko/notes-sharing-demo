package org.mk.notessharedemo.note.application.adapters.outbound;

import org.mk.notessharedemo.note.application.adapters.inbound.UserId;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthenticationServiceClient {

    static final String FIRST_ID = "001af529-5500-4879-952b-be21998ef35e";
    static final String SECOND_ID = "eedfb62a-24c0-4c33-ae69-c03b9401dd0b";
    static final String FIRST_TOKEN = "938fabe89a2cb";
    static final String SECOND_TOKEN = "be89a2cb938fa";

    private static final ConcurrentHashMap<String, UserId> USER_REPO = new ConcurrentHashMap<>() {{
        put(FIRST_TOKEN, new UserId(UUID.fromString(FIRST_ID)));
        put(SECOND_TOKEN, new UserId(UUID.fromString(SECOND_ID)));
    }};

    //b47cd176-40c2-4b77-8e2f-4b9d791394da
    //dc4ae2aa-4a7b-45ae-8b75-8916b154b3d2
    //ecf448b3-47f3-4ec0-924a-d8beb4391ed9
    public UserId authenticate(String authToken) {
        return Optional.ofNullable(USER_REPO.get(authToken))
                .orElseThrow(UserAuthenticationException::new);
    }
}
