package com.example.txtledbluetooth.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.setting.model.SettingModel;
import com.example.txtledbluetooth.setting.model.SettingModelImpl;
import com.example.txtledbluetooth.setting.view.AudioPromptsView;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;

import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-21.
 */

public class AudioPromptsPresenterImp implements AudioPromptsPresenter {
    private AudioPromptsView mPromptsView;
    private SettingModel mSettingModel;
    private Context mContext;
    private String mMacAddress;
    private UUID mServiceUUID;
    private UUID mCharacterUUID;

    public AudioPromptsPresenterImp(AudioPromptsView mPromptsView, Context mContext) {
        this.mPromptsView = mPromptsView;
        this.mContext = mContext;
        mSettingModel = new SettingModelImpl();
        String serviceUUID = SharedPreferenceUtils.getSendService(mContext);
        String characterUUID = SharedPreferenceUtils.getSendCharacter(mContext);
        if (!TextUtils.isEmpty(serviceUUID)) {
            mServiceUUID = UUID.fromString(serviceUUID);
        }
        if (!TextUtils.isEmpty(characterUUID)) {
            mCharacterUUID = UUID.fromString(characterUUID);
        }
        mMacAddress = SharedPreferenceUtils.getMacAddress(mContext);
    }

    @Override
    public void choseModel(int id) {
        String command = null;
        switch (id) {
            case R.id.item_off:
                command = BleCommandUtils.CLOSE_SOUND;
                mPromptsView.selectOff();
                break;
            case R.id.item_voice_and_tones:
                command = BleCommandUtils.OPEN_SOUND;
                mPromptsView.selectVoiceAndTones();
                break;
        }
        if (!TextUtils.isEmpty(command) && !TextUtils.isEmpty(mMacAddress)) {
            mSettingModel.WriteCommand(MyApplication.getBluetoothClient(mContext), mMacAddress,
                    mServiceUUID, mCharacterUUID, command);
        }
    }
}
