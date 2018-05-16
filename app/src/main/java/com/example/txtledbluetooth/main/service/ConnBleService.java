package com.example.txtledbluetooth.main.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.txtledbluetooth.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KomoriWu on 2017/6/24.
 */

@SuppressLint("Registered")
public class ConnBleService extends Service {
    private static final int TIMER_DELAY = 1000;
    private static final int TIMER_PERIOD = 100;
    private Timer mTimer;
    private ConnBleObservable mConnBleObservable;

    @Override
    public void onCreate() {
        super.onCreate();
        mConnBleObservable = new ConnBleObservable();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new connBleBinder();
    }

    class connBleBinder extends Binder implements ConnBleInterface {

        @Override
        public void scanBle() {
            startTimer();
        }

        @Override
        public void addObserver(Observer observer) {
            mConnBleObservable.addObserver(observer);
        }
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimer != null) {
            mTimer.schedule(new ConnTimerTask(), TIMER_DELAY, TIMER_PERIOD);
        }
    }

    class ConnTimerTask extends TimerTask {
        public void run() {
            checkBluetooth();
        }
    }

    private void checkBluetooth() {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            //得到BluetoothAdapter的Class对象
            Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;
            Method method = null;
            method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState",
                    (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);
            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Set<BluetoothDevice> devices = adapter.getBondedDevices();

                for (BluetoothDevice device : devices) {
                    Log.d("bluename", "本机---" + device.getName());
                    Log.d("bluename", "本机---" + device.getAddress());
                    if (device.getName().contains(Utils.BLE_NAME)) {
                        @SuppressLint("PrivateApi")
                        Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod(
                                "isConnected", (Class[]) null);
                        method.setAccessible(true);
                        boolean isConnected = (boolean) isConnectedMethod.invoke(device, (
                                Object[]) null);
                        if (isConnected) {
                            Log.d("bluename", "connected:" + device.getAddress());
                            mConnBleObservable.notifyChanged(device.getAddress());
                            stopTimer();
                            break;
                        }
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public class ConnBleObservable extends Observable {
        public void notifyChanged(String macAddress) {
            this.setChanged();
            this.notifyObservers(macAddress);
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            mConnBleObservable.deleteObservers();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}
