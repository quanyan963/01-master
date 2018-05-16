package com.example.txtledbluetooth.music.playing;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.base.BaseActivity;
import com.example.txtledbluetooth.bean.MusicInfo;
import com.example.txtledbluetooth.music.playing.presenter.PlayingPresenter;
import com.example.txtledbluetooth.music.playing.presenter.PlayingPresenterImpl;
import com.example.txtledbluetooth.music.playing.view.PlayingView;
import com.example.txtledbluetooth.music.service.MusicInterface;
import com.example.txtledbluetooth.music.service.MusicService;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.qiushui.blurredview.BlurredView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayingActivity extends BaseActivity implements Observer, PlayingView, SeekBar.
        OnSeekBarChangeListener {
    public static final String TAG = PlayingActivity.class.getSimpleName();
    public static final int NEEDLE_ANIM_VALUES = -25;
    public static final int NEEDLE_ANIM_DURATION = 200;
    public static final int BLURRED_LEVEL = 100;
    public static final String ROTATION = "rotation";
    @BindView(R.id.layout_activity_play)
    RelativeLayout layoutActivityPlay;
    @BindView(R.id.layout_album_cover)
    FrameLayout layoutAlbumCover;
    @BindView(R.id.layout_volume)
    LinearLayout layoutVolume;
    @BindView(R.id.layout_play)
    RelativeLayout layoutPlay;
    @BindView(R.id.layout_previous)
    RelativeLayout layoutPrevious;
    @BindView(R.id.layout_next)
    RelativeLayout layoutNext;
    @BindView(R.id.iv_needle)
    ImageView ivNeedle;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_previous)
    ImageView ivPrevious;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_album_cover)
    ImageView ivAlbumCover;
    @BindView(R.id.tv_music_name)
    TextView tvMusicName;
    @BindView(R.id.tv_singer)
    TextView tvSinger;
    @BindView(R.id.tv_time_left)
    TextView tvTimeLeft;
    @BindView(R.id.seek_bar_play)
    SeekBar seekBarPlay;
    @BindView(R.id.seek_bar_volume)
    SeekBar seekBarVolume;
    @BindView(R.id.tv_time_right)
    TextView tvTimeRight;
    @BindView(R.id.blurred_view)
    BlurredView blurredView;
    private ObjectAnimator mNeedleAnim;
    private ObjectAnimator mRotateAnim;
    private AnimatorSet mAnimatorSet;
    private int mIntentPosition;
    private String mAlbumUri;
    private List<MusicInfo> mMusicInfoList;
    private MusicInterface mMusicInterface;
    private Intent mIntent;
    private MyServiceConn mServiceConn;
    private int mCurrentPosition;
    private PlayingPresenter mPlayingPresenter;
    private boolean mIsExistPlayData;
    private AudioManager mAudioManager;

    @Override
    public void init() {
        setContentView(R.layout.activity_playing);
        ButterKnife.bind(this);
        initToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
            }
        });
        initService();
        toolbar.setBackground(null);
        tvTitle.setText(getString(R.string.now_playing));
        mIntentPosition = getIntent().getIntExtra(Utils.POSITION, 0);
        mCurrentPosition = mIntentPosition;
        mMusicInfoList = MusicInfo.listAll(MusicInfo.class);

        mPlayingPresenter = new PlayingPresenterImpl(this);
        initPlayUi(mIntentPosition);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekBarPlay.setOnSeekBarChangeListener(this);
        seekBarVolume.setOnSeekBarChangeListener(this);
        seekBarVolume.setMax(mAudioManager.getStreamMaxVolume(Utils.STREAM_TYPE));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mCurrentPosition != mIntentPosition) {
            initPlayUi(mCurrentPosition);
        }
        initAnim();
    }

    private void initPlayUi(int position) {
        if (position >= 0 && position < mMusicInfoList.size()) {
            MusicInfo musicInfo = mMusicInfoList.get(position);
            tvMusicName.setText(musicInfo.getTitle());
            tvSinger.setText(musicInfo.getArtist());
            mAlbumUri = musicInfo.getAlbumUri();
            MyApplication.getImageLoader(PlayingActivity.this).displayImage(mAlbumUri,
                    ivAlbumCover, Utils.getImageOptions(R.mipmap.placeholder_disk_play_program, 360));

            mPlayingPresenter.loadGSAlbumCover(mAlbumUri, this);
        }
    }

    private void initService() {
        mServiceConn = new MyServiceConn();
        mIntent = new Intent(this, MusicService.class);
        startService(mIntent);
        bindService(mIntent, mServiceConn, BIND_AUTO_CREATE);
    }

    @Override
    public void startAnim() {
        if (mRotateAnim == null) {
            mRotateAnim = ObjectAnimator.ofFloat(layoutAlbumCover, ROTATION, 0f, 360f);
            mRotateAnim.setDuration(10000);
            mRotateAnim.setRepeatCount(-1);//设置动画重复次数，这里-1代表无限
            mRotateAnim.setRepeatMode(Animation.ABSOLUTE);//设置动画循环模式。
            mRotateAnim.setInterpolator(new LinearInterpolator());
        }

        if (mNeedleAnim == null) {
            mNeedleAnim = ObjectAnimator.ofFloat(ivNeedle, ROTATION, NEEDLE_ANIM_VALUES, 0);
            mNeedleAnim.setDuration(NEEDLE_ANIM_DURATION);
            mNeedleAnim.setRepeatMode(0);
            mNeedleAnim.setInterpolator(new LinearInterpolator());
        }

        if (mAnimatorSet == null) {
            mAnimatorSet = new AnimatorSet();
        }
        if (!mAnimatorSet.isStarted()) {
            mAnimatorSet.play(mNeedleAnim).before(mRotateAnim);
            mAnimatorSet.start();
        }
        if (mAnimatorSet.isPaused()) {
            mAnimatorSet.resume();
        }
        ivPlay.setImageResource(R.mipmap.play_rdi_btn_play);
    }

    @Override
    public void stopAnim() {
        if (mAnimatorSet != null && !mAnimatorSet.isPaused()) {
            mNeedleAnim.start();
            mAnimatorSet.pause();
        }
        ivPlay.setImageResource(R.mipmap.play_rdi_btn_pause);
    }

    @Override
    public void showGSAlbumCover(final Bitmap bitmap) {
        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.blurred_view_alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                blurredView.setBlurredImg(bitmap);
                blurredView.setBlurredLevel(BLURRED_LEVEL);
            }
        });
        blurredView.startAnimation(alpha);
    }


    @OnClick({R.id.layout_activity_play, R.id.iv_play, R.id.layout_previous, R.id.layout_next,
            R.id.layout_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_play:
                if (layoutVolume.getVisibility() == View.GONE) {
                    layoutVolume.setVisibility(View.VISIBLE);
                    seekBarVolume.setProgress(mAudioManager.getStreamVolume(Utils.STREAM_TYPE));
                } else if (layoutVolume.getVisibility() == View.VISIBLE) {
                    layoutVolume.setVisibility(View.GONE);
                }
                break;

            case R.id.iv_play:
                if (mMusicInterface != null) {
                    if (mIsExistPlayData) {
                        if (mMusicInterface.isPlaying()) {
                            mMusicInterface.pausePlay();
                            stopAnim();
                        } else {
                            mMusicInterface.continuePlay();
                            startAnim();
                        }
                    } else {
                        if (mMusicInfoList.size() > 0 && mMusicInfoList != null) {
                            mPlayingPresenter.playMusic(mMusicInterface, mMusicInfoList.get(
                                    mCurrentPosition).getUrl());
                            startAnim();
                            mIsExistPlayData = true;
                        }
                    }

                }
                break;
            case R.id.layout_previous:
                if (mMusicInfoList.size() > 0 && mMusicInfoList != null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            stopAnim();
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            startAnim();
                            mPlayingPresenter.playMusic(mMusicInterface, mMusicInfoList.
                                    get(getPreviousSongPosition()).getUrl());
                            initPlayUi(mCurrentPosition);
                        }
                    }.execute();
                }
                break;
            case R.id.layout_next:
                if (mMusicInfoList.size() > 0 && mMusicInfoList != null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            stopAnim();
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            startAnim();
                            mPlayingPresenter.playMusic(mMusicInterface, mMusicInfoList.
                                    get(getNextSongPosition()).getUrl());
                            initPlayUi(mCurrentPosition);
                        }
                    }.execute();
                    break;
                }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getId() == R.id.seek_bar_play) {
            mPlayingPresenter.seekToPlayProgress(mMusicInterface, seekBar.getProgress());
        } else if (seekBar.getId() == R.id.seek_bar_volume) {
            mPlayingPresenter.seekToVolumeProgress(this, seekBar.getProgress());
        }
    }


    private class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMusicInterface = (MusicInterface) iBinder;
            mMusicInterface.addObserver(PlayingActivity.this);
            initAnim();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    private void initAnim() {
        if (mMusicInterface != null) {
            if (mMusicInterface.isPlaying()) {
                startAnim();
            } else {
                stopAnim();
            }
        }
    }

    @Override
    public void update(Observable observable, final Object object) {
        new AsyncTask<Void, Void, Bundle>() {
            @Override
            protected Bundle doInBackground(Void... voids) {
                return (Bundle) object;
            }

            @Override
            protected void onPostExecute(Bundle bundle) {
                super.onPostExecute(bundle);
                mIsExistPlayData = true;
                int duration = bundle.getInt(Utils.DURATION);
                int currentProgress = bundle.getInt(Utils.CURRENT_PROGRESS);
                int currentPlayPosition = bundle.getInt(Utils.CURRENT_PLAY_POSITION);
                mCurrentPosition = currentPlayPosition;

                seekBarPlay.setMax(duration);
                seekBarPlay.setProgress(currentProgress);
                tvTimeRight.setText(Utils.getPlayTime(duration));
                tvTimeLeft.setText(Utils.getPlayTime(currentProgress));
                if (currentProgress == duration) {
                    initPlayUi(getNextSongPosition());
                }

            }
        }.execute();


    }

    private int getNextSongPosition() {
        mCurrentPosition += 1;
        int nexSongPosition = mCurrentPosition;
        if (nexSongPosition >= mMusicInfoList.size()) {
            nexSongPosition = 0;
            mCurrentPosition = 0;
        }
        return nexSongPosition;
    }

    private int getPreviousSongPosition() {
        mCurrentPosition -= 1;
        int previousSongPosition = mCurrentPosition;
        if (previousSongPosition < 0) {
            previousSongPosition = mMusicInfoList.size() - 1;
            mCurrentPosition = mMusicInfoList.size() - 1;
        }
        return previousSongPosition;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferenceUtils.saveLastPlayPosition(this, mCurrentPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
