package com.example.txtledbluetooth.light.view;

import com.example.txtledbluetooth.bean.RgbColor;

/**
 * Created by KomoriWu
 * on 2017-04-25.
 */

public interface EditLightView {
    void showPopWindow();

    void setTvColor(int color);

    void revert();

    void setPaintPixel(RgbColor rgbColor);

    void onWriteFailure();
}
