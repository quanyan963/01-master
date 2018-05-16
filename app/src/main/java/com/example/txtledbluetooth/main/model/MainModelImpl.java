package com.example.txtledbluetooth.main.model;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * Created by KomoriWu
 * on 2017-04-24.
 */

public class MainModelImpl implements MainModel {
    private static final int SEARCH_TIMEOUT = 5000;
    private static final int SEARCH_TIMEOUT_NUMBER = 2;

    @Override
    public void initBle(Context context, BluetoothClient client, BleConnectOptions
            bleConnectOptions, String macAddress, OnInitBleListener onInitBleListener) {
        if (client.isBleSupported()) {
            if (client.isBluetoothOpened()) {
                connBle(client, macAddress, context, onInitBleListener,
                        bleConnectOptions);
            } else {
                onInitBleListener.onFailure(context.getString(R.string.open_ble));
            }
        } else {
            onInitBleListener.OnException(context.getString(R.string.no_support_ble));
        }

    }

    private void connBle(final BluetoothClient client, final String traditionAddress,
                         final Context context, final OnInitBleListener onInitBleListener,
                         final BleConnectOptions bleConnectOptions) {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(SEARCH_TIMEOUT, SEARCH_TIMEOUT_NUMBER).build();
        client.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Log.d("bluename", "BLE---" + device.getName());
                Log.d("bluename", "BLE---" + device.getAddress());

//                if (traditionAddress.substring(3).equals(device.getAddress().
//                        substring(3))) {
//                    client.stopSearch();
//                    //监听连接状态
//                    connStatus(client, device.getAddress(), onInitBleListener);
//
//                    connBle(context, client, bleConnectOptions, device.getAddress(),
//                            device.getName(), onInitBleListener);
//                }

                if (!device.getName().toString().equals("NULL")){
                    if (traditionAddress.substring(12).replace(":","")
                            .equals(device.getName().toString().
                                    substring(device.getName().toString().length()-4))){
                        client.stopSearch();
                        //监听连接状态
                        connStatus(client, device.getAddress(), onInitBleListener);

                        connBle(context, client, bleConnectOptions, device.getAddress(),
                                device.getName(), onInitBleListener);
                    }
                }
            }

            @Override
            public void onSearchStopped() {
                onInitBleListener.OnException(context.getString(R.string.search_stop));
            }

            @Override
            public void onSearchCanceled() {
            }

        });
    }


    private void connStatus(BluetoothClient client, String address, final OnInitBleListener
            onInitBleListener) {
        client.registerConnectStatusListener(address, new
                BleConnectStatusListener() {
                    @Override
                    public void onConnectStatusChanged(String mac, int status) {
                        onInitBleListener.onConnStatus(mac, status);
                    }
                });
    }

    private void connBle(final Context context, final BluetoothClient client, BleConnectOptions
            bleConnectOptions, final String address, final String name,
                         final OnInitBleListener onInitBleListener) {
        client.connect(address, bleConnectOptions, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
                    SharedPreferenceUtils.saveMacAddress(context, address);
                    List<BleGattService> services = profile.getServices();
                    for (BleGattService service : services) {
//                        if (service.getUUID().toString().contains(Utils.SEND_SERVICE)) {
//                            List<BleGattCharacter> characters = service.getCharacters();
//                            for (BleGattCharacter character : characters) {
//                                //save uuid
//                                SharedPreferenceUtils.saveSendService(context,
//                                        service.getUUID().toString());
//                                SharedPreferenceUtils.saveSendCharacter(context,
//                                        character.getUuid().toString());
//                            }
//                        } else if (service.getUUID().toString().contains(Utils.RECEIVE_SERVICE)) {
//                            List<BleGattCharacter> characters = service.getCharacters();
//                            for (BleGattCharacter character : characters) {
//                                //save uuid
//                                SharedPreferenceUtils.saveReceiveService(context,
//                                        service.getUUID().toString());
//                                SharedPreferenceUtils.saveReceiveCharacter(context,
//                                        character.getUuid().toString());
//
//                            }
//                        }
                        if (service.getUUID().toString().contains(Utils.SEND_SERVICE)) {
                            List<BleGattCharacter> characters = service.getCharacters();
                            for (BleGattCharacter character : characters) {
                                //save uuid
                                SharedPreferenceUtils.saveSendService(context,
                                        service.getUUID().toString());
                                SharedPreferenceUtils.saveSendCharacter(context,
                                        character.getUuid().toString());
                                //save uuid
                                SharedPreferenceUtils.saveReceiveService(context,
                                        service.getUUID().toString());
                                SharedPreferenceUtils.saveReceiveCharacter(context,
                                        character.getUuid().toString());

                            }
                        }

                    }
                    onInitBleListener.onSuccess(name);
                } else {
                    onInitBleListener.OnException(context.getString(R.string.conn_timeout));
                }
            }
        });
    }

    public interface OnInitBleListener {
        void onSuccess(String name);

        void onFailure(String message);

        void onConnStatus(String mac, int status);

        void OnException(String exception);

    }
}
