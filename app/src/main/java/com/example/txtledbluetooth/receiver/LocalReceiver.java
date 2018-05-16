package com.example.txtledbluetooth.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.txtledbluetooth.utils.Utils;
import com.nostra13.universalimageloader.utils.L;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by KomoriWu
 * on 2017-07-04.
 */

public class LocalReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
            EventBus.getDefault().post(Utils.EVENT_REFRESH_LANGUAGE);
        }
    }
}
