package com.lifepulse;

import android.app.Activity;
import android.util.Log;

import com.google.gson.JsonObject;
import com.harman.pulsesdk.ImplementPulseHandler;
import com.harman.pulsesdk.PulseColor;

public class DigitalLifeParser {
    private ImplementPulseHandler pulseHandler;
    private boolean isInRoom = false;
    public DigitalLifeParser(Activity activity) {
        pulseHandler = new ImplementPulseHandler();
        pulseHandler.ConnectMasterDevice(activity);
        pulseHandler.registerPulseNotifiedListener(new Connector());
    }

    public void parse(JsonObject event) {
        String label = event.get("label").getAsString();
        String type = event.get("type").getAsString();
        if(type.equals("event")){
            switch (label){
                case "switch-on":
                    // animate full yellow going down
                    Log.d("WATCHING", "sending signal to pulse, switch on");
                    boolean j = pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 255, (byte) 255), true);
                    break;
                case "switch-off":
                    // animate none going full yellow
                    Log.d("WATCHING", "sending signal to pulse: switch off");
                    pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 255, (byte) 0), true);
                    pulseHandler.SetBrightness(40);
                    break;
                case "tamper":
                case "bypass":
                    // animate flashing red
                    Log.d("WATCHING", "sending signal to pulse: tamper/bypass");
                    pulseHandler.SetBrightness(100);
                    pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 0, (byte) 0), true);
                    break;
                case "motion-detected":
                    // animate flashing blue
                    Log.d("WATCHING", "sending signal to pulse: motion");
                    j = pulseHandler.SetBackgroundColor(new PulseColor((byte) 0, (byte) 0, (byte) 255), true);
                    break;
                case "button-activated":
                    // make a call to Kandy
                    Activator.emptyPost("https://run-west.att.io/4095e2904b934/883c44cfa9e6/a649986eff9bc0d/in/flow/panic");
                    break;
                case "opened":
                    Log.d("WATCHING", "sending signal to pulse: open door");
                    j = pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 192, (byte) 203), true);
                    break;
                case "co-alarm":
                    Log.d("WATCHING", "sending signal to pulse: carbon monoxide");
                    for (int i = 0; i < 6; i++){
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(i % 2 == 0){
                            j = pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 255, (byte) 255), true);
                        }else{
                            j = pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 0, (byte) 0), true);
                        }
                    }
                default:
                    // don't do anything
            }
        }else{
            String value = event.get("value").getAsString();
            switch (label){
                case "multilevel":
                    // change Pulse fill to match new value
                    PulseColor[] colors = new PulseColor[99];
                    for(int i = 0; i < 99; i++){
                        if(i < Integer.parseInt(value)){
                            colors[i] = new PulseColor((byte) 0, (byte) 0, (byte) 0);
                        }else{
                            colors[i] = new PulseColor((byte) 0, (byte) 0, (byte) 255);
                        }
                    }

                    pulseHandler.SetColorImage(colors);
            }
        }

        pulseHandler.PropagateCurrentLedPattern();

    }
}
