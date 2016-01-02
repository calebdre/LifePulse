package com.lifepulse;

public class AuthenticatedEvent {

    private AuthToken token;

    public AuthenticatedEvent(AuthToken token) {
        this.token = token;
    }

    public AuthToken getToken() {
        return token;
    }
}
