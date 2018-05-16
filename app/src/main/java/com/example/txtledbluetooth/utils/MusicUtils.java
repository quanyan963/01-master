package com.example.txtledbluetooth.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KomoriWu
 * on 2017-04-28.
 */

public class MusicUtils {
    public static String[] musicMedias = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Albums.ALBUM_ID};
    public static final String MUSIC_ALBUM_URI = "content://media/external/audio/albumart";

    public static Bitmap createThumbFromUir(Context context, Uri albumUri) {
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = context.getContentResolver().openInputStream(albumUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
