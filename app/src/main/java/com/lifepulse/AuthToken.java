package com.lifepulse;

public class AuthToken {
    private String authToken, requestToken, gateway;

    public AuthToken(String authToken, String requestToken, String gateway) {
        this.authToken = authToken;
        this.requestToken = requestToken;
        this.gateway = gateway;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public String getGateway() {
        return gateway;
    }
}
