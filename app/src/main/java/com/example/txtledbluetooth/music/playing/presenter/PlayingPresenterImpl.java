package com.example.txtledbluetooth.music.playing.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.AsyncTask;

import com.example.txtledbluetooth.music.playing.model.PlayingModel;
import com.example.txtledbluetooth.music.playing.model.PlayingModelImpl;
import com.example.txtledbluetooth.music.playing.view.PlayingView;
import com.example.txtledbluetooth.music.service.MusicInterface;
import com.example.txtledbluetooth.utils.Utils;

/**
 * Created by KomoriWu
 * on 2017-05-09.
 */

public class PlayingPresenterImpl implements PlayingPresenter {
    private PlayingView mPlayingView;
    private PlayingModel mPlayingModel;

    public PlayingPresenterImpl(PlayingView mPlayingView) {
        this.mPlayingView = mPlayingView;
        mPlayingModel = new PlayingModelImpl();
    }

    @Override
    public void playMusic(final MusicInterface musicInterface, final String songUrl) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                musicInterface.play(songUrl);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mPlayingView.startAnim();
            }
        }.execute();
    }

    @Override
    public void loadGSAlbumCover(String albumUri, Context context) {
        mPlayingModel.loadGSAlbumCover(albumUri, context, new PlayingModelImpl.OnLoadListener() {
            @Override
            public void success(Bitmap bitmap) {
                mPlayingView.showGSAlbumCover(bitmap);
            }
        });
    }

    @Override
    public void seekToPlayProgress(MusicInterface musicInterface, int progress) {
        musicInterface.seekTo(progress);
    }

    @Override
    public void seekToVolumeProgress(Context context, int progress) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(Utils.STREAM_TYPE, progress, AudioManager.FLAG_PLAY_SOUND);
    }

}
