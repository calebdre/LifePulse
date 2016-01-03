package com.lifepulse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.harman.pulsesdk.ImplementPulseHandler;
import com.harman.pulsesdk.PulseColor;
import com.harman.pulsesdk.PulseThemePattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class TestActivity extends AppCompatActivity{
    @Bind(R.id.btn1) Button btn1;
    @Bind(R.id.btn2) Button btn2;
    @Bind(R.id.btn3) Button btn3;
    ImplementPulseHandler pulseHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        new PulseFinder().findPulse(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(PulseFoundEvent e){
        if(e.isFound()){
            pulseHandler = new ImplementPulseHandler();
            pulseHandler.registerPulseNotifiedListener(new Connector());
            pulseHandler.ConnectMasterDevice(this);
            Toast.makeText(this, "foudn the pulse", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "couldn't find the pulse", Toast.LENGTH_LONG).show();
            Log.d("WATCH", "couldn't find the pulse");
        }
    }

    @OnClick(R.id.btn1)
    public void onclickbtn1(){
        Log.d("watch"," button clicked");
        boolean j = pulseHandler.SetBackgroundColor(new PulseColor((byte) 255, (byte) 255, (byte) 255), true);
        boolean k = pulseHandler.SetLEDPattern(PulseThemePattern.PulseTheme_Firework);
    }
}
