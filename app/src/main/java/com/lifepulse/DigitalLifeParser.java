package com.lifepulse;

import com.google.gson.JsonObject;
import com.harman.pulsesdk.PulseThemePattern;

import java.util.ArrayList;
import java.util.List;

public class DigitalLifeParser {
    private List<DigitalLifeDevice> dlDevices;

    public DigitalLifeParser(List<DigitalLifeDevice> dlDevices) {
        this.dlDevices = dlDevices;
    }

    public void parse(JsonObject event) {
        String label = event.get("label").getAsString();
        String type = event.get("type").getAsString();
        Connector pattern = new Connector();
        if(type.equals("event")){
            switch (label){
                case "switch-on":
                        // animate full yellow going down
                    pattern.;
                    pattern.onLEDPatternChanged(PulseThemePattern.PulseTheme_Hourglass);
                    break;
                case "switch-off":
                    // animate none going full yellow
                    break;
                case "tamper":
                case "bypass":
                    // animate flashing red
                    break;
                case "motion-detected":
                    // animate flashing blue
                    break;
                case "button-activated":
                    // make a call to Kandy

                default:
                    // don't do anything
            }
        }else{
            String value = event.get("value").getAsString();
            switch (label){
                case "multilevel":
                    // change Pulse fill to match new value
            }
        }
    }
}
