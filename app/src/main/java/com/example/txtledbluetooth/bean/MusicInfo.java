package com.example.txtledbluetooth.bean;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

/**
 * Created by KomoriWu
 * on 2017-04-28.
 */
@Table(name = "music_info")
public class MusicInfo extends SugarRecord implements Serializable {
    @SerializedName("songId")
    private int songId;
    @SerializedName("title")
    private String title;
    @SerializedName("album")
    private String album;
    @SerializedName("artist")
    private String artist;
    @SerializedName("url")
    private String url;
    @SerializedName("duration")
    private int duration;
    @SerializedName("albumId")
    private int albumId;
    @SerializedName("albumUri")
    private String albumUri;
    private Bitmap albumImg;

    public MusicInfo() {
    }

    public MusicInfo(int songId, String title, String album, String artist, String url,
                     int duration, int albumId,String albumUri) {
        this.songId = songId;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.url = url;
        this.duration = duration;
        this.albumId = albumId;
        this.albumUri = albumUri;
    }

    public MusicInfo(int songId, String title, String album, String artist, String url,
                     int duration, Bitmap albumImg,String albumUri) {
        this.songId = songId;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.url = url;
        this.duration = duration;
        this.albumImg = albumImg;
        this.albumUri = albumUri;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }


    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Bitmap getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(Bitmap albumImg) {
        this.albumImg = albumImg;
    }
    public String getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(String albumUri) {
        this.albumUri = albumUri;
    }
}
