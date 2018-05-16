package com.example.txtledbluetooth.application;

import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orm.SugarApp;

/**
 * Created by KomoriWu
 * on 2017-04-18.
 */

public class MyApplication extends SugarApp {
    private static ImageLoader mImageLoader;
    private static BluetoothClient mBluetoothClient;
//    private RefWatcher refWatcher;

//    public static RefWatcher getRefWatcher(Context context) {
//        MyApplication application = (MyApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
//        refWatcher = LeakCanary.install(this);
    }

    public static ImageLoader getImageLoader(Context context) {
        if (mImageLoader == null) {
            synchronized (ImageLoader.class) {
                if (mImageLoader == null) {
                    mImageLoader = ImageLoader.getInstance();
                    mImageLoader.init(ImageLoaderConfiguration.createDefault(context.
                            getApplicationContext()));
                }
            }
        }
        return mImageLoader;
    }

    public static BluetoothClient getBluetoothClient(Context context) {
        if (mBluetoothClient == null) {
            synchronized (BluetoothClient.class) {
                if (mBluetoothClient == null) {
                    mBluetoothClient = new BluetoothClient(context);
                }
            }
        }
        return mBluetoothClient;
    }


}
