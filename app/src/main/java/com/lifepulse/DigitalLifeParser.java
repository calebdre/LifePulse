package com.lifepulse;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.google.gson.JsonObject;
import com.harman.pulsesdk.ImplementPulseHandler;
import com.harman.pulsesdk.PulseColor;

import java.util.Locale;

public class DigitalLifeParser {
    private ImplementPulseHandler pulseHandler;
    private boolean isInRoom = false;
    TextToSpeech ts;

    public DigitalLifeParser(Activity activity) {
        pulseHandler = new ImplementPulseHandler();
        pulseHandler.ConnectMasterDevice(activity);
        pulseHandler.registerPulseNotifiedListener(new Connector());

        ts = new TextToSpeech(activity.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    ts.setLanguage(Locale.UK);
                    Log.d("WATCHING", "text to speech inittialized");

                    ts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            Log.d("WATCHING", "started reading text: " + utteranceId);
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            Log.d("WATCHING", "done reading text: " + utteranceId);
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.d("WATCHING", "error reading text: " + utteranceId);
                        }
                    });
                }else{
                    Log.d("WATCHING", String.valueOf(status));
                    Log.d("WATCHING", "text to speech  not inittialized");
                }
            }
        });
    }

    public ImplementPulseHandler getPulseHandler() {
        return pulseHandler;
    }

    public TextToSpeech getTs() {
        return ts;
    }

    public void parse(JsonObject event) {
        String label = event.get("label").getAsString();
        String type = event.get("type").getAsString();
        if(type.equals("event")){
            switch (label){
                case "switch-on":
                    // animate full yellow going down
                    Log.d("WATCHING", "sending signal to pulse, switch on");
                    ts.speak("Lights on", TextToSpeech.QUEUE_FLUSH, null, "switch on");
                    boolean j = pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 255, (byte) 255), true);
                    break;
                case "switch-off":
                    // animate none going full yellow
                    Log.d("WATCHING", "sending signal to pulse: switch off");
                    ts.speak("Lights off", TextToSpeech.QUEUE_FLUSH, null, "off");
                    pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 255, (byte) 0), true);
                    pulseHandler.SetBrightness(40);
                    break;
                case "tamper":
                case "bypass":
                    // animate flashing red
                    Log.d("WATCHING", "sending signal to pulse: tamper/bypass");
                    ts.speak("Water levels risen", TextToSpeech.QUEUE_FLUSH, null, "tamper");
                    pulseHandler.SetBrightness(100);
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
                    break;
                case "motion-detected":
                    // animate flashing blue
                    Log.d("WATCHING", "sending signal to pulse: motion");
                    ts.speak("Someone is at your door", TextToSpeech.QUEUE_FLUSH, null, "motion");
                    j = pulseHandler.SetBackgroundColor(new PulseColor((byte) 0, (byte) 0, (byte) 255), true);
                    break;
                case "button-activated":
                    // make a call to Kandyx
                    break;
                case "door-unlocked":
                    ts.speak("Door unlocked", TextToSpeech.QUEUE_FLUSH, null, "open");
                    Log.d("WATCHING", "sending signal to pulse: open door");
                    j = pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 255, (byte) 255), true);
                    break;
                case "opened":
                    ts.speak("Garage Door opened", TextToSpeech.QUEUE_FLUSH, null, "open");
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
                    break;
                case "door-locked":
                    ts.speak("Door Locked", TextToSpeech.QUEUE_FLUSH, null, "open");
                    j = pulseHandler.SetBackgroundColor(new PulseColor((byte) 0, (byte) 255, (byte) 60), true);
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
                    break;
                case "SM04034B30 MotionClear":
                    for (int i = 0; i < 6; i++){
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(i % 2 == 0){
                            pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 255, (byte) 255), true);
                        }else{
                            pulseHandler.SetBackgroundColor(new PulseColor((byte) 0, (byte) 0, (byte) 0), true);
                        }
                    }
                    break;
                case "contact-state":
                    if(value.equals("open")){
                        ts.speak("Door Opened", TextToSpeech.QUEUE_FLUSH, null, "open");
                        pulseHandler.SetBackgroundColor(new PulseColor((byte) 0, (byte) 255, (byte) 60), true);
                    }else{
                        ts.speak("Door Closed", TextToSpeech.QUEUE_FLUSH, null, "open");
                        pulseHandler.SetBackgroundColor(new PulseColor((byte) 40, (byte) 26, (byte) 13), true);
                    }
                    break;
                default:

            }
        }

        pulseHandler.PropagateCurrentLedPattern();

    }
}
