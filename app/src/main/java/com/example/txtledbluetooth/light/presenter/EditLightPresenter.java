package com.example.txtledbluetooth.light.presenter;

import android.os.Bundle;
import android.view.View;

import com.example.txtledbluetooth.bean.RgbColor;


/**
 * Created by KomoriWu
 * on 2017-04-25.
 */

public interface EditLightPresenter {
    void viewOnclick(View clickView, View bgView, String sqlName, int position);

    void setIsSetOnColorSelectListener(boolean isSetOnColorSelectListener);

    void setLightSpeed(String lightNo, int speed, boolean isWrite);

    void setLightSpeed(String lightNo, int speed);

    void setLightBrightness(String lightNo, int brightness, boolean isWrite);

    void setLightBrightness(String lightNo, int brightness);

    void operateItemBluetooth(String lightName, int position, int popupPosition, boolean isWrite);

    void operateItemBluetooth(String lightName, int position, int popupPosition);

    void operateSwitchBluetooth(String lightNo, boolean isChecked, boolean isWrite);

    void operateSwitchBluetooth(String lightNo, boolean isChecked);

    void updateLightColor(String lightNo, int viewPosition, String color, Bundle data);

    void saveLightType(Bundle bundle);

    int getLightType(String name);

    RgbColor getLightColor(String sqlName, int position);

    void writeCommand();

    void saveDefaultColors(String sqlName);
}
