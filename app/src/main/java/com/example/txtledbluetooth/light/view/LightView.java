package com.example.txtledbluetooth.light.view;

import android.os.Bundle;

import com.example.txtledbluetooth.bean.Lighting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KomoriWu
 * on 2017-04-22.
 */

public interface LightView {
    void showLightData(ArrayList<Lighting> lightingList, List<Boolean> list);

    void editLight(int id);

    void onWriteFailure();

    void onNotify(Bundle bundle);
}
