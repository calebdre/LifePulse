package com.lifepulse;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import com.harman.pulsesdk.DeviceModel;
import com.harman.pulsesdk.ImplementPulseHandler;
import com.harman.pulsesdk.PulseColor;
import com.harman.pulsesdk.PulseNotifiedListener;
import com.harman.pulsesdk.PulseThemePattern;
import java.util.Timer;

//purpose is to connect to the pulse
public class Connector implements PulseNotifiedListener {
    static String Tag = "PulseDemo";
    Timer mTimer=null;
    boolean isConnectBT;
    boolean isActive = true;
    //FragMic fragMic;
    //FragCamera fragCamera;
    //FragPattern fragPattern;



    public ImplementPulseHandler pulseHandler = new ImplementPulseHandler();



    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height",
                        "dimen", "android"));
    }

    public void setBackground(){
//        pulseHander.SetBackgroundColor(new PulseColor(back_r, back_g, back_b), backSlave);
//        break;

    }

    public void pick_color(){
        pulseHandler.CaptureColorFromColorPicker();
    }

    public void record_sound(View v){
        pulseHandler.GetMicrophoneSoundLevel();
    }


//    public void setTimer()
//    {
//        if(mTimer!=null)
//            return;
//
//        mTimer=new Timer();
//        TimerTask task=new TimerTask()
//        {
//            @Override
//            public void run() {
//                PulseDemo.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public synchronized void run() {
//                        if (isActive) {
//                            pulseHandler.ConnectMasterDevice(PulseDemo.this);
//                        }
//                    }
//                });
//            }
//        };
//        mTimer.schedule(task, 1000, 1500);
//    }

    private void cancelTimer()
    {
        if(mTimer!=null)
        {
            mTimer.cancel();
            mTimer=null;
        }
    }

    @Override
    public void onConnectMasterDevice() {
        Log.i(Tag, "onConnectMasterDevice");
        isConnectBT = true;
        cancelTimer();
    }

    @Override
    public void onDisconnectMasterDevice() {
        Log.i(Tag, "onDisconnectMasterDevice");
        isConnectBT = false;
        //setTimer();

    }

    @Override
    public void onLEDPatternChanged(PulseThemePattern pattern) {
        //Toast.makeText(this, "onLEDPatternChanged:" + pattern.name(), Toast.LENGTH_SHORT);
        Log.i(Tag, "onLEDPatternChanged");

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }

    @Override
    public void onSoundEvent(final int soundLevel) {
        //Toast.makeText(this, "onSoundEvent: level=" + soundLevel, Toast.LENGTH_SHORT);
        Log.i(Tag, "soundLevel:"+soundLevel);

    }

    @Override
    public void onRetCaptureColor(final PulseColor capturedColor) {
//        Toast.makeText(this,
//                "onRetCaptureColor: red=" + capturedColor.red + " green=" + capturedColor.green + " blue=" + capturedColor.blue,
//                Toast.LENGTH_SHORT);

    }

    @Override
    public void onRetCaptureColor(byte red, byte green, byte blue) {
        //Toast.makeText(this, "onRetCaptureColor1: red=" + red + " green=" + green + " blue=" + blue, Toast.LENGTH_SHORT);
    }

    @Override
    public void onRetSetDeviceInfo(boolean ret) {
        //Toast.makeText(this, "onRetSetDeviceInfo:"+ret, Toast.LENGTH_SHORT);
    }

    @Override
    public void onRetGetLEDPattern(PulseThemePattern pattern) {
        //Toast.makeText(this, "onRetGetLEDPattern:" + (pattern== null ? "null":pattern.name()), Toast.LENGTH_SHORT);
    }

    @Override
    public void onRetRequestDeviceInfo(DeviceModel[] deviceModel) {
        //Toast.makeText(this, "onRetRequestDeviceInfo:"+deviceModel.toString(), Toast.LENGTH_SHORT);
    }

    @Override
    public void onRetSetLEDPattern(boolean b) {
        Log.i(Tag, "onRetSetLEDPattern:"+b);
//        if(b && fragPattern != null && fragPattern.isBroadcastSlave){
//            pulseHandler.PropagateCurrentLedPattern();
//        }
    }

    @Override
    public void onRetBrightness(int i) {

    }
}
