package com.lifepulse;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.greenrobot.event.EventBus;

public class PulseFinder {

    public void findPulse(Context context){
        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.enable();
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(device.getName().equalsIgnoreCase("JBL Pulse 2")){
                        Class class1 = null;
                        Method createBondMethod = null;
                        try {
                            device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
                            device.getClass().getMethod("cancelPairingUserInput").invoke(device);
                            class1 = Class.forName("android.bluetooth.BluetoothDevice");
                            createBondMethod = class1.getMethod("createBond");
                            createBondMethod.invoke(device);
                            adapter.cancelDiscovery();
                            context.unregisterReceiver(this);
                            EventBus.getDefault().post(new PulseFoundEvent());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        if(adapter.getBondedDevices().size() == 0){
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(receiver, filter);
            adapter.startDiscovery();
        }
    }
}
