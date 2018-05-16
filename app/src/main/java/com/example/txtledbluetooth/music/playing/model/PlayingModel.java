package com.example.txtledbluetooth.music.playing.model;

import android.content.Context;

import com.example.txtledbluetooth.main.model.MainModelImpl;

/**
 * Created by KomoriWu
 * on 2017-05-09.
 */

public interface PlayingModel {
    void loadGSAlbumCover(String albumUri,Context context, PlayingModelImpl.OnLoadListener
            onLoadListener);

}
