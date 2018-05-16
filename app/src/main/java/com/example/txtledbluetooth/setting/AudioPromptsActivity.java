package com.example.txtledbluetooth.setting;

import android.content.Intent;
import android.view.View;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.base.BaseActivity;
import com.example.txtledbluetooth.setting.presenter.AudioPromptsPresenter;
import com.example.txtledbluetooth.setting.presenter.AudioPromptsPresenterImp;
import com.example.txtledbluetooth.setting.view.AudioPromptsView;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.example.txtledbluetooth.widget.ItemLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioPromptsActivity extends BaseActivity implements AudioPromptsView {
    @BindView(R.id.item_off)
    ItemLayout itemOff;
    @BindView(R.id.item_voice_and_tones)
    ItemLayout itemVoiceAndTones;
    private AudioPromptsPresenter mPromptsPresenter;
    private String[] mModels;

    @Override
    public void init() {
        setContentView(R.layout.activity_audio_prompts);
        ButterKnife.bind(this);
        mModels = getResources().getStringArray(R.array.audio_prompts_model);
        initToolbar();
        tvTitle.setText(getString(R.string.audio_prompts));
        initItemBgColor();
        mPromptsPresenter = new AudioPromptsPresenterImp(this, this);
        itemOff.setOnItemListener(new ItemLayout.OnItemListener() {
            @Override
            public void onClickItemListener(View v) {
                v.setId(R.id.item_off);
                mPromptsPresenter.choseModel(v.getId());
            }
        });
        itemVoiceAndTones.setOnItemListener(new ItemLayout.OnItemListener() {
            @Override
            public void onClickItemListener(View v) {
                v.setId(R.id.item_voice_and_tones);
                mPromptsPresenter.choseModel(v.getId());
            }
        });
    }

    private void initItemBgColor() {
        String itemText = mModels[SharedPreferenceUtils.getAudioPromptsModel(this)];
        if (itemText.equals(itemOff.getTvLeftStr())) {
            selectOffEffect();
        } else if (itemText.equals(itemVoiceAndTones.getTvLeftStr())) {
            selectVoiceAndTonesEffect();
        }
    }


    @Override
    public void selectOff() {
        selectOffEffect();
        resultModel(itemOff);
    }


    @Override
    public void selectVoiceAndTones() {
        selectVoiceAndTonesEffect();
        resultModel(itemVoiceAndTones);
    }

    private void selectOffEffect() {
        itemOff.setIsItemSelected(true);
        itemVoiceAndTones.setIsItemSelected(false);
    }


    private void selectVoiceAndTonesEffect() {
        itemOff.setIsItemSelected(false);
        itemVoiceAndTones.setIsItemSelected(true);
    }

    private void resultModel(ItemLayout itemLayout) {
        Intent intent = getIntent();
        intent.putExtra(Utils.ITEM_RIGHT_TEXT, itemLayout.getTvLeftStr());
        setResult(RESULT_OK, intent);
        finish();
    }


}
