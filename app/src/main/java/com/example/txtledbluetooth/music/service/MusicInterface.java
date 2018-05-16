package com.example.txtledbluetooth.music.service;

import android.os.Handler;

import java.util.Observer;

/**
 * Created by KomoriWu
 * on 2017-05-04.
 */

public interface MusicInterface {

    void play(String songUrl);

    void pausePlay();

    void continuePlay();

    void seekTo(int progress);

    boolean isPlaying();
    void addObserver(Observer observer);
}
