package com.lifepulse;

public class PulseFoundEvent {
    private boolean isFound;

    public PulseFoundEvent(boolean isFound) {
        this.isFound = isFound;
    }

    public boolean isFound() {
        return isFound;
    }
}
