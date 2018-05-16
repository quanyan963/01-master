package com.example.txtledbluetooth.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.txtledbluetooth.bean.MusicInfo;
import com.example.txtledbluetooth.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KomoriWu
 * on 2017-05-04.
 */

public class MusicService extends Service {
    public static final int UPDATE_PERIOD = 1000;
    public static final int UPDATE_DELAY = 500;
    private MediaPlayer mMediaPlayer;
    private Timer mTimer;
    private MusicObservable mMusicObservable;
    private int mCurrentPlayPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicObservable = new MusicObservable();
        mMediaPlayer = new MediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl();
    }

    class MusicControl extends Binder implements MusicInterface {

        @Override
        public void play(String songUrl) {
            try {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                }

                try {
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(songUrl);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IllegalStateException e) {
                    mMediaPlayer = null;
                    mMediaPlayer = new MediaPlayer();
                }
                List<MusicInfo> musicInfoList = MusicInfo.listAll(MusicInfo.class);
                for (int i = 0; i < musicInfoList.size(); i++) {
                    if (songUrl.equals(musicInfoList.get(i).getUrl())) {
                        mCurrentPlayPosition = i;
                    }
                }
                addTimer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void pausePlay() {
            if (mMediaPlayer.isPlaying() && mMediaPlayer != null) {
                mMediaPlayer.pause();
            }
        }

        @Override
        public void continuePlay() {
            if (!mMediaPlayer.isPlaying() && mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        }

        @Override
        public void seekTo(int progress) {
            if (mMediaPlayer != null) {
                mMediaPlayer.seekTo(progress);
            }
        }

        @Override
        public boolean isPlaying() {
            if (mMediaPlayer != null) {
                return mMediaPlayer.isPlaying();
            } else {
                return false;
            }

        }

        @Override
        public void addObserver(Observer observer) {
            mMusicObservable.addObserver(observer);
        }


    }

    private void addTimer() {
        //如果没有创建计时器对象
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    int duration = mMediaPlayer.getDuration();
                                    duration -= 3000;
                                    int currentProgress = mMediaPlayer.getCurrentPosition();
                                    if (currentProgress > duration) {
                                        currentProgress = duration;
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(Utils.DURATION, duration);
                                    bundle.putInt(Utils.CURRENT_PROGRESS, currentProgress);
                                    bundle.putInt(Utils.CURRENT_PLAY_POSITION, mCurrentPlayPosition);
                                    mMusicObservable.notifyChanged(bundle);
                                }
                            },
                    UPDATE_DELAY, UPDATE_PERIOD);
        }
    }


    public class MusicObservable extends Observable {
        public void notifyChanged(Bundle bundle) {
            this.setChanged();
            this.notifyObservers(bundle);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

}
