package com.example.txtledbluetooth.sources.model;

import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.inuker.bluetooth.library.BluetoothClient;

import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-06-23.
 */

public class SourcesModelImpl implements SourcesModel {
    @Override
    public void WriteCommand(BluetoothClient client, String macAddress, UUID serviceUUID,
                             UUID characterUUID, String command) {
        BleCommandUtils.divideFrameBleSendData(command.getBytes(), client,
                macAddress, serviceUUID, characterUUID, null);
    }
}
