package com.example.txtledbluetooth.music.playing.view;

import android.graphics.Bitmap;

/**
 * Created by KomoriWu
 * on 2017-05-09.
 */

public interface PlayingView {

    void startAnim();

    void stopAnim();

    void showGSAlbumCover(Bitmap bitmap);
}
