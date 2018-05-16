package com.example.txtledbluetooth.main.presenter;

import android.content.Context;

import com.example.txtledbluetooth.main.model.MainModelImpl;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;

/**
 * Created by KomoriWu
 * on 2017-04-19.
 */

public interface MainPresenter {
    void connBle(Context context,String macAddress);

    void switchNavigation(int id);
}
