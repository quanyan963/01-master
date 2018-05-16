package com.example.txtledbluetooth.light.presenter;

/**
 * Created by KomoriWu
 * on 2017-04-22.
 */

public interface LightPresenter {
    void operateItemBluetooth(String lightName, int id);
    void operateItemSeekBar(String lightName, int id);
    void operateTvRightBluetooth(int id);
    void operateSwitchBluetooth(boolean isChecked);
    void openNotify();
    void showLightData();
    void writeCommand();
}
