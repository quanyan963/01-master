package com.example.txtledbluetooth.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LocaleUtils {

    public static void setAutoLanguage(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = Locale.getDefault();
        res.updateConfiguration(conf, dm);
    }

}
