package com.example.txtledbluetooth.music.playing.presenter;

import android.content.Context;
import android.os.Bundle;

import com.example.txtledbluetooth.music.service.MusicInterface;

/**
 * Created by KomoriWu
 * on 2017-05-09.
 */

public interface PlayingPresenter {
    void playMusic(MusicInterface musicInterface, String songUrl);
    void loadGSAlbumCover(String albumUri,Context context);
    void seekToPlayProgress(MusicInterface musicInterface,int progress);
    void seekToVolumeProgress(Context context,int progress);
}
