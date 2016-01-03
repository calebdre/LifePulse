package com.lifepulse;

import java.util.List;

public class DigitalLifeDevice {
    private String deviceType, deviceGuid;
    private List<DeviceEvent> events;
    private List<DeviceAttribute> attributes;

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceGuid() {
        return deviceGuid;
    }

    public List<DeviceEvent> getEvents() {
        return events;
    }

    public List<DeviceAttribute> getAttributes() {
        return attributes;
    }
}
