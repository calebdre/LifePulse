package com.lifepulse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    Activator activator = new Activator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PulseFinder().findPulse(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onEvent(AuthenticatedEvent e){
        AuthToken token = e.getToken();
        activator.getDevices(token);
    }

    public void onEvent(PulseFoundEvent e){
        activator.authenticate();
    }

    public void onEvent(DevicesReceivedEvent e){
        List<DigitalLifeDevice> devices = e.getDevices();
        FlowPoler poller = new FlowPoler();
        try {
            poller.startStream(devices);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
