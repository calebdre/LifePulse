package com.lifepulse;

import java.util.List;

public class DevicesReceivedEvent {
    private List<DigitalLifeDevice> devices;

    public DevicesReceivedEvent(List<DigitalLifeDevice> devices) {
        this.devices = devices;
    }

    public List<DigitalLifeDevice> getDevices() {
        return devices;
    }
}
