package com.example.txtledbluetooth.setting.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.setting.model.SettingModel;
import com.example.txtledbluetooth.setting.model.SettingModelImpl;
import com.example.txtledbluetooth.setting.view.SettingView;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.inuker.bluetooth.library.BluetoothClient;

import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-21.
 */

public class SettingPresenterImp implements SettingPresenter {
    private SettingView mSettingView;
    private SettingModel mSettingModel;
    private Context mContext;
    private String mMacAddress;
    private UUID mServiceUUID;
    private UUID mCharacterUUID;
    private BluetoothClient mClient;

    public SettingPresenterImp(SettingView mSettingView, Context mContext) {
        this.mSettingView = mSettingView;
        this.mContext = mContext;
        mSettingModel = new SettingModelImpl();
        mClient = MyApplication.getBluetoothClient(mContext);
    }

    private void initConnData() {
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
    public void settings(int id) {
        switch (id) {
            case R.id.item_audio_prompts:
                mSettingView.setAudioPrompts();
                break;
            case R.id.item_reset:
                mSettingView.showResetDialog();
                break;
        }
    }

    @Override
    public void resetToDefault() {
        String command = BleCommandUtils.RESET;
        writeCommand(command);
        mSettingModel.cleanSql(mContext);
        SharedPreferenceUtils.cleanClickPosition(mContext);
        SharedPreferenceUtils.saveIsResetDefault(mContext, true);
    }

    private void writeCommand(String command) {
        initConnData();
        if (!TextUtils.isEmpty(command) && !TextUtils.isEmpty(mMacAddress)) {
            mSettingModel.WriteCommand(mClient, mMacAddress,
                    mServiceUUID, mCharacterUUID, command);
        }
    }
}
