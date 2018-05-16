package com.example.txtledbluetooth.main.service;

import com.google.common.util.concurrent.Service;

import java.util.Observer;
import java.util.Timer;

/**
 * Created by KomoriWu on 2017/6/24.
 */

public interface ConnBleInterface {
   void scanBle();
   void addObserver(Observer observer);
}
