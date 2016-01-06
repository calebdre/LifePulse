package com.lifepulse;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.harman.pulsesdk.ImplementPulseHandler;
import com.harman.pulsesdk.PulseColor;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextToSpeech ts;
    private Activity activity;
    private ImplementPulseHandler pulseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        button = (Button) findViewById(R.id.thing);
        new PulseFinder().findPulse(this, new PulseFinder.PulseFoundCallback() {
            @Override
            public void onPulseFind(boolean isFound) {
                Toast.makeText(activity, "Pulse found status: "+ isFound, Toast.LENGTH_LONG);
            }
        });

        initPulse();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 255, (byte) 255), true);
            }
        });
    }

    private void initPulse() {
        pulseHandler = new ImplementPulseHandler();
        pulseHandler.ConnectMasterDevice(activity);
        pulseHandler.registerPulseNotifiedListener(new Connector());

        ts = new TextToSpeech(activity.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
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
                } else {
                    Log.d("WATCHING", String.valueOf(status));
                    Log.d("WATCHING", "text to speech  not inittialized");
                }
            }});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
