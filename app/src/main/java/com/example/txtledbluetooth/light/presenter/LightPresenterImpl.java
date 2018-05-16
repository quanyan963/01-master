package com.example.txtledbluetooth.light.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.Lighting;
import com.example.txtledbluetooth.light.model.LightModel;
import com.example.txtledbluetooth.light.model.LightModelImpl;
import com.example.txtledbluetooth.light.view.LightView;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

/**
 * Created by KomoriWu
 * on 2017-04-22.
 */

public class LightPresenterImpl implements LightPresenter {
    private LightView mLightView;
    private Context mContext;
    private LightModel mLightModel;
    private String mMacAddress;
    private UUID mServiceUUID;
    private UUID mCharacterUUID;
    private BluetoothClient mClient;
    private StringBuffer mStringCommands;
    private int mCommandCount;

    public LightPresenterImpl(LightView mLightView, Context mContext) {
        this.mLightView = mLightView;
        this.mContext = mContext;
        mLightModel = new LightModelImpl();
        mClient = MyApplication.getBluetoothClient(mContext);
        mStringCommands = new StringBuffer(BleCommandUtils.HEAD);
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
    public void operateItemBluetooth(String lightName, int position) {
        String command = BleCommandUtils.getItemCommandByType(mContext, position, lightName);
        dealCommand(command);
        SharedPreferenceUtils.saveClickPosition(mContext, position);
    }

    @Override
    public void operateItemSeekBar(String lightName, int position) {
        HashMap<String, Integer> hashMap = LightType.getSbProgressMap(lightName, position);
        int bright = hashMap.get(Utils.SEEK_BAR_PROGRESS_BRIGHT);
        int speed = hashMap.get(Utils.SEEK_BAR_PROGRESS_SPEED);
        String lightNo = BleCommandUtils.getLightNo(position);
        dealCommand(BleCommandUtils.getLightBrightCommand(lightNo, Integer.toHexString(bright)));
        if (Utils.isSBarSpeedVisible(position)) {
            dealCommand(BleCommandUtils.getLightSpeedCommand(lightNo, Integer.toHexString(speed)));
        }
        operateSwitchBluetooth(lightNo, lightName);
    }

    private void operateSwitchBluetooth(String lightNo, String lightName) {
        boolean isChecked = LightType.getPulseIsOpen(lightName);
        String command = BleCommandUtils.musicPulseSwitch(lightNo, isChecked);
        dealCommand(command);
    }

    @Override
    public void operateTvRightBluetooth(int id) {
        mLightView.editLight(id);
    }

    @Override
    public void operateSwitchBluetooth(boolean isChecked) {
        String command = isChecked ? BleCommandUtils.OPEN_LIGHT : BleCommandUtils.CLOSE_LIGHT;
        writeCommand(command);
    }

    @Override
    public void openNotify() {
        String serviceUUID = SharedPreferenceUtils.getReceiveService(mContext);
        String characterUUID = SharedPreferenceUtils.getReceiveCharacter(mContext);
        UUID uuidService = null, uuidCharacter = null;
        if (!TextUtils.isEmpty(serviceUUID)) {
            uuidService = UUID.fromString(serviceUUID);
        }
        if (!TextUtils.isEmpty(characterUUID)) {
            uuidCharacter = UUID.fromString(characterUUID);
        }
        mMacAddress = SharedPreferenceUtils.getMacAddress(mContext);
        if (!TextUtils.isEmpty(mMacAddress)) {
            mLightModel.openNotify(mContext, mClient, mMacAddress, uuidService, uuidCharacter,
                    new LightModelImpl.OnInterfaceOpenNotify() {
                        @Override
                        public void onNotify(Bundle bundle) {
                            mLightView.onNotify(bundle);
                        }

                        @Override
                        public void onOpenNotifySuccess() {
//                            writeCommand(BleCommandUtils.BACK_NOTIFY);
                        }

                    });

        }
    }

    @Override
    public void showLightData() {
        new initDataAsyncTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class initDataAsyncTask extends AsyncTask<Void, Void, ArrayList<Lighting>> {
        ArrayList<Lighting> lightingList;
        List<Boolean> list;

        @Override
        protected ArrayList<Lighting> doInBackground(Void... voids) {
            lightingList = Utils.getLightList(mContext);
            list = new ArrayList<>();
            for (int i = 0; i < lightingList.size(); i++) {
                if (SharedPreferenceUtils.getClickPosition(mContext) == i) {
                    list.add(true);
                } else {
                    list.add(false);
                }
            }
            return lightingList;
        }

        @Override
        protected void onPostExecute(ArrayList<Lighting> lightingArrayList) {
            mLightView.showLightData(lightingArrayList, list);
        }
    }

    private void dealCommand(String command) {
        String[] commands = command.split("\\" + BleCommandUtils.
                DIVISION);
        mStringCommands.append(commands[2] + BleCommandUtils.SEMICOLON);
        mCommandCount++;
    }

    @Override
    public void writeCommand() {
        mStringCommands.replace(3, 4, mCommandCount + "");
        mStringCommands.replace(mStringCommands.toString().length() - 1,
                mStringCommands.toString().length(), BleCommandUtils.END_MARK);
        writeCommand(mStringCommands.toString());
        mCommandCount = 0;
        mStringCommands = new StringBuffer(BleCommandUtils.HEAD);
    }

    private void writeCommand(String command) {
        initConnData();
        if (!TextUtils.isEmpty(command) && !TextUtils.isEmpty(mMacAddress)) {
            mLightModel.WriteCommand(mClient, mMacAddress,
                    mServiceUUID, mCharacterUUID, command, new
                            LightModelImpl.OnInterfaceWriteCommand() {
                                @Override
                                public void onWriteFailure() {
                                    mLightView.onWriteFailure();
                                }
                            });
        }

    }
}
