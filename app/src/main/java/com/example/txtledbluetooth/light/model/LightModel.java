package com.example.txtledbluetooth.light.model;

import android.content.Context;
import android.os.Bundle;

import com.example.txtledbluetooth.bean.RgbColor;
import com.inuker.bluetooth.library.BluetoothClient;

import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public interface LightModel {
    void WriteCommand(BluetoothClient client, String macAddress, UUID serviceUUID,
                      UUID characterUUID, String command, LightModelImpl.OnInterfaceWriteCommand
                              onInterfaceWriteCommand);

    void openNotify(Context context,BluetoothClient client, String macAddress, UUID serviceUUID,
                    UUID characterUUID, LightModelImpl.OnInterfaceOpenNotify
                            onInterfaceOpenNotify);

    void saveLightColor(Bundle bundle);

    void saveLightType(Bundle bundle);

    int getLightType(String name);

    RgbColor getLightColor(String sqlName, int position);
    void saveDefaultColors(String sqlName);
}
