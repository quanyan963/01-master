package com.example.txtledbluetooth.setting.model;

import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;

import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-28.
 */

public interface SettingModel {
    void WriteCommand(BluetoothClient client, String macAddress, UUID serviceUUID,
                      UUID characterUUID, String command);

    void cleanSql(Context context);
}
