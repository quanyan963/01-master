package com.example.txtledbluetooth.music.model;

import android.content.Context;
import android.database.Cursor;

import com.example.txtledbluetooth.bean.MusicInfo;

import java.util.ArrayList;

/**
 * Created by KomoriWu
 * on 2017-04-28.
 */

public interface MusicModel {
    ArrayList<MusicInfo> scanMusic(Context context, Cursor cursor);
}
