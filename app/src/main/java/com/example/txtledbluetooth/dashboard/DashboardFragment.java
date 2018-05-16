package com.example.txtledbluetooth.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by KomoriWu
 * on 2017-04-20.
 */

public class DashboardFragment extends BaseFragment {

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
