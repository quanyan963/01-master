package com.example.txtledbluetooth.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.media.AudioManager;
import android.view.inputmethod.InputMethodManager;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.bean.Lighting;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by KomoriWu
 * on 2017-04-18.
 */

public class Utils {
    public static final String ITEM_RIGHT_TEXT = "item_right_text";
    public static final String BLE_NAME = "GP";//Creative HALO
    public static final String RECEIVE_SERVICE = "6677";
    public static final String SEND_SERVICE = "faa0";  //调试 ffe0  7777
    public static final String LIGHT_MODEL_NAME = "light_model_name";
    public static final String LIGHT_MODEL_ID = "light_model_id";
    public static final String DURATION = "duration";
    public static final String CURRENT_PROGRESS = "current_progress";
    public static final String CURRENT_PLAY_POSITION = "current_play_position";
    public static final String POPUP_POSITION = "popup_position";
    public static final String POSITION = "position";
    public static final String SQL_NAME = "sql_name";
    public static final String COLOR_R = "color_r";
    public static final String COLOR_G = "color_g";
    public static final String COLOR_B = "color_b";
    public static final int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    public static final String SEEK_BAR_PROGRESS_BRIGHT = "bright_progress";
    public static final String SEEK_BAR_PROGRESS_SPEED = "speed_progress";
    public static final String PULSE_IS_OPEN = "pulse_is_open";
    public static final String SWITCH_STATE = "switch_state";
    private static final int SEEK_BAR_BRIGHT_MAX = 255;
    private static final int SEEK_BAR_SPEED_MAX = 10;
    public static final String EVENT_REFRESH_LANGUAGE = "event_refresh_language";


    public static DisplayImageOptions getImageOptions(int defaultIconId) {
        return getImageOptions(defaultIconId, 0);
    }

    public static DisplayImageOptions getImageOptions(int defaultIconId, int cornerRadiusPixels) {
        return new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
                .showImageOnLoading(defaultIconId)
                .showImageOnFail(defaultIconId)
                .showImageForEmptyUri(defaultIconId)
                .cacheInMemory(true)
                .cacheOnDisc()
                .build();
    }

    public static void showAlertDialog(Context context, String message) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public static ArrayList<Lighting> getLightList(Context context) {
        String[] lightNames = context.getResources().getStringArray(R.array.lighting_name);
        int[] lightIcons = {R.mipmap.icon_moon_light, R.mipmap.icon_fireworks,
                R.mipmap.icon_hot_wheels, R.mipmap.icon_spectrum, R.mipmap.icon_full_spectrum,
                R.mipmap.icon_pulsate, R.mipmap.icon_morph, R.mipmap.icon_beat_meter,
                R.mipmap.icon_wave, R.mipmap.icon_wave, R.mipmap.icon_mood, R.mipmap.icon_aurora};
        ArrayList<Lighting> lightingList = new ArrayList<>();
        for (int i = 0; i < lightIcons.length; i++) {
            lightingList.add(i, new Lighting(lightNames[i], lightIcons[i], true));
        }
        return lightingList;
    }


    public static BleConnectOptions getBleConnectOptions() {
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(10000)   // 连接超时10s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(10000)  // 发现服务超时20s
                .build();
        return options;
    }

    public static String[] getPopWindowItems(Context context, int position) {
        String[] items;
        switch (position) {
            case 0:
                items = context.getResources().getStringArray(R.array.moonlight_color_type);
                break;
            case 1:
            case 2:
                items = context.getResources().getStringArray(R.array.hot_wheels_color_type);
                break;
            case 3:
            case 7:
            case 11:
                items = context.getResources().getStringArray(R.array.spectrum_color_type);
                break;
            case 4:
                items = context.getResources().getStringArray(R.array.full_spectrum_color_type);
                break;
            case 5:
                items = context.getResources().getStringArray(R.array.pulsate_color_type);
                break;
            case 6:
                items = context.getResources().getStringArray(R.array.morph_color_type);
                break;
            case 8:
                items = context.getResources().getStringArray(R.array.wave_color_type);
                break;
            case 9:
                items = context.getResources().getStringArray(R.array.full_wave_color_type);
                break;
            case 10:
                items = context.getResources().getStringArray(R.array.mood_color_type);
                break;

            default:
                items = context.getResources().getStringArray(R.array.wave_color_type);
                break;


        }
        return items;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String getPermission(int position) {
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        return permissions[position];
    }

    public static String getPlayTime(int time) {
        int musicTime = time / 1000;
        int min = musicTime / 60 < 0 ? 0 : musicTime / 60;
        String minute = min < 10 ? "0" + min : min + "";
        int s = musicTime % 60 < 0 ? 0 : musicTime % 60;
        String second = s < 10 ? "0" + s : s + "";
        return minute + ":" + second;
    }


    public static HashMap getSeekBarProgress(int position) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        int bright = 0;
        int speed = 0;
        switch (position) {
            case 0:
                bright = (int) (SEEK_BAR_BRIGHT_MAX * 0.5);
                break;
            case 1:
                bright = (int) (SEEK_BAR_BRIGHT_MAX * 0.5);
                speed = (int) (SEEK_BAR_SPEED_MAX * 0.25);
                break;
            case 2:
            case 3:
            case 4:
                bright = (int) (SEEK_BAR_BRIGHT_MAX * 0.5);
                speed = (int) (SEEK_BAR_SPEED_MAX * 0.5);
                break;
            case 5:
                bright = (int) (SEEK_BAR_BRIGHT_MAX * 0.25);
                speed = (int) (SEEK_BAR_SPEED_MAX * 0.25);
                break;
            case 6:
            case 7:
                speed = (int) (SEEK_BAR_SPEED_MAX * 0.25);
                bright = (int) (SEEK_BAR_BRIGHT_MAX * 0.75);
                break;
            case 10:
                bright = SEEK_BAR_BRIGHT_MAX;
                break;
            case 8:
            case 9:
            case 11:
                bright = SEEK_BAR_BRIGHT_MAX;
                speed = (int) (SEEK_BAR_SPEED_MAX * 0.5);
                break;
        }
        hashMap.put(SEEK_BAR_PROGRESS_BRIGHT, bright);
        hashMap.put(SEEK_BAR_PROGRESS_SPEED, speed);
        return hashMap;
    }

    public static boolean isSBarSpeedVisible(int position) {
        boolean isVisible = true;
        switch (position) {
            case 0:
            case 7:
            case 9:
            case 10:
            case 11:
                isVisible = false;
                break;
        }
        return isVisible;
    }


    public static boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.
                NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider) return true;
        return false;
    }


    public static int getItemPosition(int position) {
        return switchBlePosition(position);
    }

    private static int switchBlePosition(int blePosition) {
        int[] blePositions = {2, 3, 4, 5, 7, 6, 9, 12, 13, 14, 16, 15};
        for (int i = 0; i < blePositions.length; i++) {
            if (blePosition == blePositions[i]) {
                return i;
            }
        }
        //默认停留在moonlight
        return 0;
    }

    public static WheelView.WheelViewStyle getWheelViewStyle(Context context) {
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextSize = 28;
        style.textSize = 20;
        style.backgroundColor = context.getResources().getColor(R.color.content_bg);
        style.textColor = context.getResources().getColor(R.color.text_color);
        style.selectedTextColor = context.getResources().getColor(R.color.white);
        style.holoBorderColor = context.getResources().getColor(R.color.item_selected_bg);
        return style;
    }

}
