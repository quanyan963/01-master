package com.example.txtledbluetooth.light.model;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.RgbColor;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SqlUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;

import java.util.List;
import java.util.UUID;

import static com.orm.SugarRecord.save;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public class LightModelImpl implements LightModel {

    @Override
    public void WriteCommand(BluetoothClient client, String macAddress, UUID serviceUUID,
                             UUID characterUUID, String command,
                             OnInterfaceWriteCommand onInterfaceWriteCommand) {
        BleCommandUtils.divideFrameBleSendData(command.getBytes(), client,
                macAddress, serviceUUID, characterUUID, onInterfaceWriteCommand);
    }

    @Override
    public void openNotify(final Context context, BluetoothClient client, String macAddress,
                           UUID serviceUUID, UUID characterUUID, final OnInterfaceOpenNotify
                                   onInterfaceOpenNotify) {

        client.notify(macAddress, serviceUUID, characterUUID, new BleNotifyResponse() {
            StringBuffer sbCommand = new StringBuffer();

            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                sbCommand.append(new String(value));
                if (value.length > 1) {
                    if ((value[value.length - 1] == 10 && value[value.length - 2] == 13)) {
                        Log.d("notify", "command:" + sbCommand.toString());
                        String[] commands = sbCommand.toString().split("\\" + BleCommandUtils.
                                DIVISION);
                        int blePosition = Integer.parseInt(commands[2]);
                        int position = Utils.getItemPosition(Integer.parseInt(commands[2]));
                        boolean switchState = blePosition != 0;
                        Bundle bundle = new Bundle();
                        bundle.putInt(Utils.POSITION, position);
                        bundle.putBoolean(Utils.SWITCH_STATE, switchState);
                        onInterfaceOpenNotify.onNotify(bundle);
                        sbCommand.setLength(0);
                    }
                }
            }

            @Override
            public void onResponse(int code) {
                if (code == Constants.REQUEST_SUCCESS) {
                    onInterfaceOpenNotify.onOpenNotifySuccess();
                }
            }
        });
    }

    @Override
    public void saveLightColor(final Bundle bundle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = bundle.getString(Utils.SQL_NAME);
                int r = bundle.getInt(Utils.COLOR_R);
                int g = bundle.getInt(Utils.COLOR_G);
                int b = bundle.getInt(Utils.COLOR_B);
                RgbColor rgbColor = new RgbColor(name, r, g, b);
                rgbColor.deleteRgbColorByName();
                rgbColor.save();
            }
        }).start();
    }

    @Override
    public void saveLightType(final Bundle bundle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = bundle.getString(Utils.SQL_NAME);
                int popupPosition = bundle.getInt(Utils.POPUP_POSITION);
                int bright = bundle.getInt(Utils.SEEK_BAR_PROGRESS_BRIGHT);
                int speed = bundle.getInt(Utils.SEEK_BAR_PROGRESS_SPEED);
                boolean isOpen = bundle.getBoolean(Utils.PULSE_IS_OPEN);
                LightType lightType = new LightType(name, speed, bright, popupPosition, isOpen);
                lightType.deleteLightTypeByName();
                lightType.save();
            }
        }).start();
    }

    @Override
    public int getLightType(String name) {
        List<LightType> lightTypeList = LightType.getLightTypeList(name);
        if (lightTypeList != null && lightTypeList.size() > 0) {
            LightType lightType = lightTypeList.get(0);
            return lightType.getPopupPosition();
        } else {
            return 0;
        }
    }

    @Override
    public RgbColor getLightColor(String sqlName, int position) {
        List<RgbColor> rgbColorList = RgbColor.getRgbColorList(sqlName + position);
        if (rgbColorList == null || rgbColorList.size() == 0) {
            rgbColorList = SqlUtils.getDefaultColors(sqlName, position);
        }
        return rgbColorList.size() > 0 ? rgbColorList.get(0) : null;
    }

    @Override
    public void saveDefaultColors(final String sqlName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 8; i++) {
                    RgbColor rgbColor = getLightColor(sqlName, i);
                    if (rgbColor != null) {
                        rgbColor.setName(SqlUtils.DEFAULT_COLORS + sqlName + i);
                        rgbColor.deleteRgbColorByName();
                        rgbColor.save();
                    }
                }
                SqlUtils.saveDefaultColors();
            }
        }).start();
    }

    public interface OnInterfaceWriteCommand {
        void onWriteFailure();
    }

    public interface OnInterfaceOpenNotify {
        void onNotify(Bundle bundle);

        void onOpenNotifySuccess();
    }

}
