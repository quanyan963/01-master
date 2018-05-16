package com.example.txtledbluetooth;

import android.test.InstrumentationTestCase;
import android.util.Log;

/**
 * Created by KomoriWu
 * on 2017-05-17.
 */

public class TestSubject extends ExampleInstrumentedTest {
    public void testWz() {
        int i = 1;
        int k = 2;
        if (i == k) {
            Log.d("info", "Observable completed");
        } else {
            Log.d("info", "Observable ");
        }
    }
}
