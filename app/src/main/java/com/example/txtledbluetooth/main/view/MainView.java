package com.example.txtledbluetooth.main.view;

/**
 * Created by KomoriWu
 * on 2017-04-19.
 */

public interface MainView {
    void showProgress();

    void switchDashboard();

    void switchSources();

    void switchMusic();

    void switchLighting();

    void switchSettings();

    void switchAbout();
    void switchClock();

    void hideProgress();

    void showLoadSuccessMsg(String name);

    void showLoadFailMsg(String message);

    void showLoadExceptionMsg(String exception);

    void onConnStatus(String mac, int status);

}
