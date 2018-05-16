package com.example.txtledbluetooth.light.presenter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.bean.RgbColor;
import com.example.txtledbluetooth.light.model.LightModel;
import com.example.txtledbluetooth.light.model.LightModelImpl;
import com.example.txtledbluetooth.light.view.EditLightView;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.widget.ColorPicker;

import java.util.UUID;

/**
 * Created by KomoriWu
 * on 2017-04-25.
 */

public class EditLightPresenterImpl implements EditLightPresenter, ColorPicker.OnColorChangedListener {
    private EditLightView mEditLightView;
    private ColorPicker mColorPicker;
    private Context mContext;
    private View mBgView;
    private boolean mIsSetOnColorSelectListener;
    private LightModel mLightModel;
    private String mMacAddress;
    private UUID mServiceUUID;
    private UUID mCharacterUUID;
    private StringBuffer mStringCommands;
    private int mCommandCount;

    public EditLightPresenterImpl(Context mContext, EditLightView mEditLightView,
                                  ColorPicker mColorPicker) {
        this.mContext = mContext;
        this.mEditLightView = mEditLightView;
        this.mColorPicker = mColorPicker;
        mColorPicker.setOnColorSelectListener(this);

        mLightModel = new LightModelImpl();
        String serviceUUID = SharedPreferenceUtils.getSendService(mContext);
        String characterUUID = SharedPreferenceUtils.getSendCharacter(mContext);
        if (!TextUtils.isEmpty(serviceUUID)) {
            mServiceUUID = UUID.fromString(serviceUUID);
        }
        if (!TextUtils.isEmpty(characterUUID)) {
            mCharacterUUID = UUID.fromString(characterUUID);
        }
        mMacAddress = SharedPreferenceUtils.getMacAddress(mContext);
        mStringCommands = new StringBuffer(BleCommandUtils.HEAD);
    }

    @Override
    public void viewOnclick(View clickView, View bgView, String sqlName, int position) {
        mBgView = bgView;
        switch (clickView.getId()) {
            case R.id.tv_chose_color_type:
                mEditLightView.showPopWindow();
                break;
            case R.id.tv_toolbar_right:
                mEditLightView.revert();
                break;
            case R.id.rg_color_board:
                mEditLightView.setPaintPixel(getLightColor(sqlName, position));
                break;
        }
    }

    @Override
    public void setIsSetOnColorSelectListener(boolean isSetOnColorSelectListener) {
        mIsSetOnColorSelectListener = isSetOnColorSelectListener;
    }

    @Override
    public void setLightSpeed(String lightNo, int speed) {
        setLightSpeed(lightNo, speed, true);
    }

    @Override
    public void setLightSpeed(String lightNo, int speed, boolean isWrite) {
        String command = BleCommandUtils.getLightSpeedCommand(lightNo, Integer.toHexString(speed));
        dealCommand(isWrite, command);
    }

    @Override
    public void setLightBrightness(String lightNo, int brightness) {
        setLightBrightness(lightNo, brightness, true);
    }

    @Override
    public void setLightBrightness(String lightNo, int brightness, boolean isWrite) {
        String command = BleCommandUtils.getLightBrightCommand(lightNo, Integer.
                toHexString(brightness));
        dealCommand(isWrite, command);
    }

    @Override
    public void operateItemBluetooth(String lightName, int position, int popupPosition) {
        operateItemBluetooth(lightName, position, popupPosition, true);
    }

    @Override
    public void operateItemBluetooth(String lightName, int position, int popupPosition,
                                     boolean isWrite) {
        String command = BleCommandUtils.getItemCommandByType(mContext, position, popupPosition,
                lightName);
        dealCommand(isWrite, command);
    }

    @Override
    public void operateSwitchBluetooth(String lightNo, boolean isChecked) {
        operateSwitchBluetooth(lightNo, isChecked, true);
    }

    @Override
    public void operateSwitchBluetooth(String lightNo, boolean isChecked, boolean isWrite) {
        String command = BleCommandUtils.musicPulseSwitch(lightNo, isChecked);
        dealCommand(isWrite, command);
    }

    private void dealCommand(boolean isWrite, String command) {
        if (isWrite) {
            writeCommand(command);
        } else {
            String[] commands = command.split("\\" + BleCommandUtils.
                    DIVISION);
            mStringCommands.append(commands[2] + BleCommandUtils.SEMICOLON);
            mCommandCount++;
        }
    }

    @Override
    public void updateLightColor(String lightNo, int position, String color, Bundle data) {
        String command = BleCommandUtils.updateLightColor(lightNo, position, color);
        writeCommand(command);
        mLightModel.saveLightColor(data);
    }

    @Override
    public void saveLightType(Bundle bundle) {
        mLightModel.saveLightType(bundle);
    }

    @Override
    public int getLightType(String name) {
        return mLightModel.getLightType(name);
    }

    @Override
    public RgbColor getLightColor(String sqlName, int position) {
        return mLightModel.getLightColor(sqlName, position);
    }

    @Override
    public void onColorSelect(int color) {
        if (mIsSetOnColorSelectListener) {
            mBgView.setBackgroundColor(color);
            mEditLightView.setTvColor(color);
        }
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

    @Override
    public void saveDefaultColors(String sqlName) {
        mLightModel.saveDefaultColors(sqlName);
    }

    private void writeCommand(String command) {
        if (!TextUtils.isEmpty(command) && !TextUtils.isEmpty(mMacAddress)) {
            mLightModel.WriteCommand(MyApplication.getBluetoothClient(mContext), mMacAddress,
                    mServiceUUID, mCharacterUUID, command,
                    new LightModelImpl.OnInterfaceWriteCommand() {
                        @Override
                        public void onWriteFailure() {
                            mEditLightView.onWriteFailure();
                        }
                    });
        }
    }


}
