package com.example.txtledbluetooth.music.view;

import com.example.txtledbluetooth.bean.MusicInfo;

import java.util.ArrayList;

/**
 * Created by KomoriWu
 * on 2017-04-28.
 */

public interface MusicView {
    void showMusics(ArrayList<MusicInfo> musicInfoList);


    void updateTextView(String songUrl);

    void showProgress();

    void hideProgress();
}
