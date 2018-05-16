package com.example.txtledbluetooth.music.model;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.example.txtledbluetooth.bean.MusicInfo;
import com.example.txtledbluetooth.utils.MusicUtils;

import java.util.ArrayList;

/**
 * Created by KomoriWu
 * on 2017-04-28.
 */

public class MusicModelImpl implements MusicModel {
    private ArrayList<MusicInfo> mMusicInfoList;
    public static final int LIMIT_DURATION = 10 * 1000;

    public ArrayList<MusicInfo> scanMusic(final Context context, final Cursor cursor) {
        mMusicInfoList = new ArrayList<MusicInfo>();
        if (cursor != null) {
            MusicInfo.deleteAll(MusicInfo.class);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MusicUtils.
                        musicMedias[0]));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MusicUtils.
                        musicMedias[1]));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MusicUtils.
                        musicMedias[2]));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MusicUtils.
                        musicMedias[3]));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MusicUtils.
                        musicMedias[4]));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MusicUtils.
                        musicMedias[5]));
                int albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MusicUtils.
                        musicMedias[6]));
                Uri albumUri = ContentUris.withAppendedId(Uri.parse(
                        MusicUtils.MUSIC_ALBUM_URI), albumId);
                if (duration > LIMIT_DURATION) {
                    mMusicInfoList.add(new MusicInfo(id, title, album, artist, url, duration,
                            MusicUtils.createThumbFromUir(context, albumUri), albumUri + ""));
                    MusicInfo musicInfo = new MusicInfo(id, title, album, artist, url, duration,
                            albumId, albumUri + "");
                    musicInfo.save();
                }
            }
        }
        cursor.close();
        return mMusicInfoList;
    }
}
