package com.example.txtledbluetooth.music.playing.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.txtledbluetooth.utils.MusicUtils;

/**
 * Created by KomoriWu
 * on 2017-05-09.
 */

public class PlayingModelImpl implements PlayingModel {

    @SuppressLint("StaticFieldLeak")
    @Override
    public void loadGSAlbumCover(final String albumUri, final Context context,
                                 final OnLoadListener onLoadListener) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap bitmap = MusicUtils.createThumbFromUir(context, Uri.parse(albumUri));
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                onLoadListener.success(bitmap);
            }
        }.execute();
    }

    public interface OnLoadListener {
        void success(Bitmap bitmap);
    }
}
