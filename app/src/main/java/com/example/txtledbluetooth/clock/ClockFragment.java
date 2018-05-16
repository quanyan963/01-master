package com.example.txtledbluetooth.clock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.base.BaseFragment;
import com.example.txtledbluetooth.utils.Utils;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.adapter.SimpleWheelAdapter;
import com.wx.wheelview.common.WheelData;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KomoriWu
 * on 2017-07-10.
 */


public class ClockFragment extends BaseFragment {
    @BindView(R.id.wheel_view1)
    WheelView wheelView1;
    @BindView(R.id.wheel_view2)
    WheelView<String> wheelView2;
    @BindView(R.id.wheel_view3)
    WheelView<String> wheelView3;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, null);
        ButterKnife.bind(this, view);
        initWheelView();
        return view;
    }

    private void initWheelView() {
        setWheelViewIcon(wheelView1);
        setWheelView(wheelView2, 24);
        setWheelView(wheelView3, 60);
    }

    private void setWheelView(WheelView<String> wheelView, int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        wheelView.setWheelAdapter(new ArrayWheelAdapter(getActivity()));
        wheelView.setWheelSize(5);
        wheelView.setStyle(Utils.getWheelViewStyle(getActivity()));
        wheelView.setSkin(WheelView.Skin.Holo);
        wheelView.setWheelData(list);
    }

    private void setWheelViewIcon(WheelView wheelView) {
        ArrayList<WheelData> list = new ArrayList<>();
        list.add(new WheelData(R.mipmap.icon_nav_clock, ""));
        list.add(new WheelData(R.mipmap.icon_nav_clock, ""));
        wheelView.setWheelAdapter(new IconWheelAdapter(getActivity()));
        wheelView.setWheelSize(5);
        wheelView.setStyle(Utils.getWheelViewStyle(getActivity()));
        wheelView.setSkin(WheelView.Skin.Holo);
        wheelView.setWheelData(list);
    }


}
