package com.example.txtledbluetooth.sources.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.setting.model.SettingModel;
import com.example.txtledbluetooth.setting.model.SettingModelImpl;
import com.example.txtledbluetooth.setting.view.SettingView;
import com.example.txtledbluetooth.sources.model.SourcesModel;
import com.example.txtledbluetooth.sources.model.SourcesModelImpl;
import com.example.txtledbluetooth.sources.view.SourcesView;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.inuker.bluetooth.library.BluetoothClient;

import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-06-23.
 */

public class SourcesPresenterImpl  implements SourcesPresenter{
    private SourcesView mSourcesView;
    private SourcesModel mSourcesModel;
    private Context mContext;
    private String mMacAddress;
    private UUID mServiceUUID;
    private UUID mCharacterUUID;
    private BluetoothClient mClient;

    public SourcesPresenterImpl(SourcesView mSourcesView, Context mContext) {
        this.mSourcesView = mSourcesView;
        this.mContext = mContext;
        mSourcesModel = new SourcesModelImpl();
        mClient = MyApplication.getBluetoothClient(mContext);
    }

    @Override
    public void settings(int id) {
        switch (id) {
            case R.id.item_bluetooth:
                writeCommand(BleCommandUtils.INPUT_BLUETOOTH);
                mSourcesView.chooseItem(true);
                break;
            case R.id.item_aux_in:
                writeCommand(BleCommandUtils.INPUT_AUX);
                mSourcesView.chooseItem(false);
                break;
        }
    }

    @Override
    public void writeCommand(String command) {
        initConnData();
        if (!TextUtils.isEmpty(command) && !TextUtils.isEmpty(mMacAddress)) {
            mSourcesModel.WriteCommand(mClient, mMacAddress,
                    mServiceUUID, mCharacterUUID, command);
        }
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
}
