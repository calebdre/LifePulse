package com.lifepulse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    Activator activator = new Activator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        new PulseFinder().findPulse(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(AuthenticatedEvent e){
        AuthToken token = e.getToken();
        FlowPoler poller = new FlowPoler();
        poller.startPollStream();
    }

    public void onEvent(PulseFoundEvent e){
        activator.authenticate();
    }
}
