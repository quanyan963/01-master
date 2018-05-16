package com.example.txtledbluetooth.main.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.main.model.MainModel;
import com.example.txtledbluetooth.main.model.MainModelImpl;
import com.example.txtledbluetooth.main.view.MainView;
import com.example.txtledbluetooth.utils.Utils;

import java.util.HashMap;

/**
 * Created by KomoriWu
 * on 2017-04-19.
 */

public class MainPresenterImpl implements MainPresenter, MainModelImpl.OnInitBleListener {
    private MainModelImpl mMainModel;
    private MainView mMainView;

    public MainPresenterImpl(MainView mMainView) {
        mMainModel = new MainModelImpl();
        this.mMainView = mMainView;
    }


    @Override
    public void connBle(Context context, String macAddress) {
        mMainView.showProgress();
        mMainModel.initBle(context, MyApplication.getBluetoothClient(context), Utils.
                getBleConnectOptions(),macAddress, this);
    }

    @Override
    public void switchNavigation(int id) {
        switch (id) {
//            case R.id.nav_item_dashboard:
//                mMainView.switchDashboard();
//                break;
            case R.id.nav_item_sources:
                mMainView.switchSources();
                break;
            case R.id.nav_item_music:
                mMainView.switchMusic();
                break;
            case R.id.nav_item_lighting:
                mMainView.switchLighting();
                break;
            case R.id.nav_item_setting:
                mMainView.switchSettings();
                break;
            case R.id.nav_item_about:
                mMainView.switchAbout();
                break;
//            case R.id.nav_item_clock:
//                mMainView.switchClock();
//                break;
        }
    }

    @Override
    public void onSuccess(String name) {
        mMainView.hideProgress();
        mMainView.showLoadSuccessMsg(name);
    }

    @Override
    public void onFailure(String message) {
        mMainView.hideProgress();
        mMainView.showLoadFailMsg(message);
    }

    @Override
    public void onConnStatus(String mac, int status) {
        mMainView.onConnStatus(mac, status);
    }

    @Override
    public void OnException(String exception) {
        mMainView.hideProgress();
        mMainView.showLoadExceptionMsg(exception);
    }
}
