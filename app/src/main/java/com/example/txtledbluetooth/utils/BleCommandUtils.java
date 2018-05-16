package com.example.txtledbluetooth.utils;

import android.content.Context;
import android.util.Log;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.RgbColor;
import com.example.txtledbluetooth.light.model.LightModelImpl;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public class BleCommandUtils {
    public static final String DIVISION = "$";
    private static final String SINGLE_COMMAND = "1$";
    public static final String HEAD = "G2" + DIVISION + SINGLE_COMMAND;
    public static final String END_MARK = "$\r\n";
    private static final String COLON = ":";
    public static final String SEMICOLON = ";";

    //灯光模式指令
    private static final String LIGHT = "l";
    private static final String MOON_LIGHT = "mol";
    private static final String FIREWORKS = "fwk";
    private static final String HOT_WHEELS = "hwl";
    private static final String SPECTRUM = "rbw";
    private static final String FULL_SPECTRUM = "frb";
    private static final String PULSATE = "clg";
    private static final String MORPH = "mop";
    private static final String BEAT_METER = "hst";
    private static final String WAVE = "ibw";
    private static final String FULL_WAVE = "crw";
    private static final String MOOD = "mod";
    private static final String AURORA = "aur";

    //指令类型
    private static final String COLOR_DATA = ",a,";
    private static final String MODIFY_COLOR = ",s,";
    private static final String LIGHT_SPEED = "espd,";
    private static final String LIGHT_BRIGHT = "elux,";
    private static final String PULSE_MUSIC = "ebtm,";

    //其他设置
    public static final String RESET = HEAD + "erst" + END_MARK;
    public static final String CLOSE_SOUND = HEAD + "esvt:0" + END_MARK;
    public static final String OPEN_SOUND = HEAD + "esvt:1" + END_MARK;
    public static final String INPUT_BLUETOOTH="AT+AS=BT\r\n";
    public static final String INPUT_AUX="AT+AS=AUX\r\n";

    //开关指令
    private static final String SWITCH = "etof,all:";
    public static final String CLOSE_LIGHT = HEAD + SWITCH + "0" + END_MARK;
    public static final String OPEN_LIGHT = HEAD + SWITCH + "1" + END_MARK;

    //灯光速度
    public static String getLightSpeedCommand(String lightNo, String speedHex) {
        if (speedHex.length() < 2) {
            speedHex = "0" + speedHex;
        }
        return HEAD + LIGHT_SPEED + lightNo + COLON + speedHex + END_MARK;
    }

    //灯光亮度
    public static String getLightBrightCommand(String lightNo, String brightHex) {
        if (brightHex.length() < 2) {
            brightHex = "0" + brightHex;
        }
        return HEAD + LIGHT_BRIGHT + lightNo + COLON + brightHex + END_MARK;
    }

    public static String getLightNo(int position) {
        String lightNo = "";
        switch (position) {
            case 0:
                lightNo = MOON_LIGHT;
                break;
            case 1:
                lightNo = FIREWORKS;
                break;
            case 2:
                lightNo = HOT_WHEELS;
                break;
            case 3:
                lightNo = SPECTRUM;
                break;
            case 4:
                lightNo = FULL_SPECTRUM;
                break;
            case 5:
                lightNo = PULSATE;
                break;
            case 6:
                lightNo = MORPH;
                break;
            case 7:
                lightNo = BEAT_METER;
                break;
            case 8:
                lightNo = WAVE;
                break;
            case 9:
                lightNo = FULL_WAVE;
                break;
            case 10:
                lightNo = MOOD;
                break;
            case 11:
                lightNo = AURORA;
                break;
        }
        return lightNo;
    }


    public static String getItemCommandByType(Context context, int position, String lightName) {
        return getItemCommandByType(context, position, -1, lightName);
    }

    public static String getItemCommandByType(Context context, int position, int popupPosition,
                                              String lightName) {
        StringBuffer command = new StringBuffer();
        if (popupPosition == -1) {
            List<LightType> lightTypeList = LightType.getLightTypeList(lightName);
            if (lightTypeList != null && lightTypeList.size() > 0) {
                LightType lightType = lightTypeList.get(0);
                popupPosition = lightType.getPopupPosition();
            } else {
                popupPosition = 0;
            }
        }
        //popup item 为 random 时，执行color-7
        if ((position == 1 || position == 2) && popupPosition == 0) {
            popupPosition = 3;
        }

        String[] popupItems = Utils.getPopWindowItems(context, position);
        String[] colors = RgbColor.getRgbColorStr(lightName + popupItems[popupPosition]);
        int count = getColorCount(popupItems[popupPosition], position);
        for (int i = 0; i < count; i++) {
            command.append("#" + colors[i]);
        }
        if (count == 0) {
            command.append("*");
        }
        return HEAD + LIGHT + getLightNo(position) + COLOR_DATA + popupPosition + COLON +
                command.toString() + END_MARK;
    }

    private static int getColorCount(String popupItem, int position) {
        int count = 0;
        if (popupItem.contains("1") || position == 0 || position == 8 || popupItem.contains(
                "Moonlight")) {
            count = 1;
        } else if (popupItem.contains("2")) {
            count = 2;
        } else if (popupItem.contains("3")) {
            count = 3;
        } else if (popupItem.contains("7")) {
            count = 7;
        } else if (popupItem.contains("8")) {
            count = 8;
        } else if (position == 7) {
            count = 5;
        } else if (position == 10) {
            count = 4;
        }
        return count;
    }

    public static String updateLightColor(String lightNo, int position, String color) {
        return HEAD + LIGHT + lightNo + MODIFY_COLOR + position + COLON +
                "#" + color + END_MARK;
    }

    //分包
    public static void divideFrameBleSendData(byte[] data, BluetoothClient client,
                                              final String macAddress, final UUID serviceUUID,
                                              final UUID characterUUID, final LightModelImpl.
            OnInterfaceWriteCommand onInterfaceWriteCommand) {
        Log.d("BLE Write Command:", new String(data));
        int tmpLen = data.length;
        int start = 0;
        int end = 0;
        final boolean[] isShowDialog = {true};
        while (tmpLen > 0) {
            byte[] sendData = new byte[21];
            if (tmpLen >= 20) {
                end += 20;
                sendData = Arrays.copyOfRange(data, start, end);
                start += 20;
                tmpLen -= 20;
            } else {
                end += tmpLen;
                sendData = Arrays.copyOfRange(data, start, end);
                tmpLen = 0;
            }

            client.write(macAddress, serviceUUID, characterUUID, sendData,
                    new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            if (code == -1 && isShowDialog[0]) {
                                Log.d("tag", "code:" + code);
                                Log.d("tag", "macAddress:" + macAddress);
                                Log.d("tag", "serviceUUID:" + serviceUUID);
                                Log.d("tag", "characterUUID:" + characterUUID);
                                onInterfaceWriteCommand.onWriteFailure();
                                isShowDialog[0] = false;
                            }
                        }
                    });

        }
    }

    public static String musicPulseSwitch(String lightNo, boolean isChecked) {
        String command = isChecked ? "1" : "0";
        return HEAD + PULSE_MUSIC + lightNo + ":" + command + END_MARK;
    }
}
