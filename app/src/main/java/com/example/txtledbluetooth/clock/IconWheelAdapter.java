package com.example.txtledbluetooth.clock;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.widget.WheelItem;
import com.wx.wheelview.adapter.BaseWheelAdapter;

/**
 * Created by KomoriWu
 * on 2017-07-10.
 */

public class IconWheelAdapter extends BaseWheelAdapter {
    private Context mContext;

    public IconWheelAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected View bindView(int i, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            convertView = new WheelItem(this.mContext);
        }

        WheelItem item = (WheelItem)convertView;
        item.setImage(R.mipmap.icon_nav_clock);
        return convertView;
    }

}
