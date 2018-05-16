package com.example.txtledbluetooth.music.presenter;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;

import com.example.txtledbluetooth.music.service.MusicInterface;

/**
 * Created by KomoriWu
 * on 2017-04-28.
 */

public interface MusicPresenter {
    void scanMusic(Context context);
    void playMusic(MusicInterface musicInterface,String songUrl);
}
