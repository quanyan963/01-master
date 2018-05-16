package com.example.txtledbluetooth.setting.model;

import android.content.Context;

import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.RgbColor;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SqlUtils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-28.
 */

public class SettingModelImpl implements SettingModel {
    @Override
    public void WriteCommand(BluetoothClient client, String macAddress,
                             UUID serviceUUID, UUID characterUUID, String command) {
        BleCommandUtils.divideFrameBleSendData(command.getBytes(), client,
                macAddress, serviceUUID, characterUUID, null);
    }

    @Override
    public void cleanSql(Context context) {
        RgbColor.deleteAll(RgbColor.class);
        LightType.deleteAll(LightType.class);
        SqlUtils.saveDefaultColors(context);
    }
}
