package org.mk.notessharedemo.note.application.adapters.inbound;

import lombok.RequiredArgsConstructor;
import org.mk.notessharedemo.note.application.adapters.outbound.AuthenticationServiceClient;
import org.mk.notessharedemo.note.domain.model.UserFlyweight;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AuthenticationServiceFacade {
    private final AuthenticationServiceClient authService;

    UserFlyweight authenticate(String authToken) {
        UserId userId = authService.authenticate(authToken);
        return UserFlyweight.of(userId.get());
    }
}
