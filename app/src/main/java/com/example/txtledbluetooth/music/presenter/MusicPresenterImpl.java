package com.example.txtledbluetooth.music.presenter;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.UiThread;

import com.example.txtledbluetooth.bean.MusicInfo;
import com.example.txtledbluetooth.music.model.MusicModel;
import com.example.txtledbluetooth.music.model.MusicModelImpl;
import com.example.txtledbluetooth.music.service.MusicInterface;
import com.example.txtledbluetooth.music.view.MusicView;

import java.util.ArrayList;

/**
 * Created by KomoriWu
 * on 2017-04-28.
 */

public class MusicPresenterImpl implements MusicPresenter {
    private MusicModel mMusicModel;
    private MusicView mMusicView;

    public MusicPresenterImpl(MusicView mMusicView) {
        this.mMusicView = mMusicView;
        mMusicModel = new MusicModelImpl();
    }

    @Override
    public void scanMusic(final Context context) {
        final Cursor cursor = context.getContentResolver().query(MediaStore.Audio.
                        Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);

        new AsyncTask<Void, Void, ArrayList<MusicInfo>>() {
            @Override
            protected void onPreExecute() {
                mMusicView.showProgress();
            }

            @Override
            protected ArrayList<MusicInfo> doInBackground(Void... voids) {
                return mMusicModel.scanMusic(context, cursor);
            }

            @Override
            protected void onPostExecute(ArrayList<MusicInfo> musicInfo) {
                mMusicView.showMusics(musicInfo);
                mMusicView.hideProgress();
            }
        }.execute();

    }

    @Override
    public void playMusic(final MusicInterface musicInterface,
                          final String songUrl) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                musicInterface.play(songUrl);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mMusicView.updateTextView(songUrl);
            }
        }.execute();
    }
}
