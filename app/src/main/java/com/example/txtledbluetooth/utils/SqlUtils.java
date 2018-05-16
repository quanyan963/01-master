package com.example.txtledbluetooth.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.bean.RgbColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KomoriWu
 * on 2017-05-16.
 */

public class SqlUtils {
    public static final String DEFAULT_COLORS = "default_colors";

    public static void saveDefaultColors(Context context) {
        String[] itemNames = context.getResources().getStringArray(R.array.lighting_name);

        new RgbColor(DEFAULT_COLORS + itemNames[0] + Utils.getPopWindowItems(context, 0)[0] + 0,
                255, 255, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[0] + Utils.getPopWindowItems(context, 0)[1] + 0,
                19, 248, 255).save();

        new RgbColor(DEFAULT_COLORS + itemNames[1] + Utils.getPopWindowItems(context, 1)[1] + 0,
                255, 0, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[1] + Utils.getPopWindowItems(context, 1)[2] + 0,
                255, 0, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[1] + Utils.getPopWindowItems(context, 1)[2] + 1,
                0, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[1] + Utils.getPopWindowItems(context, 1)[2] + 2,
                0, 0, 255).save();

        new RgbColor(DEFAULT_COLORS + itemNames[2] + Utils.getPopWindowItems(context, 2)[1] + 0,
                0, 0, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[2] + Utils.getPopWindowItems(context, 2)[2] + 0,
                255, 0, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[2] + Utils.getPopWindowItems(context, 2)[2] + 1,
                0, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[2] + Utils.getPopWindowItems(context, 2)[2] + 2,
                0, 0, 255).save();

        new RgbColor(DEFAULT_COLORS + itemNames[5] + Utils.getPopWindowItems(context, 5)[1] + 0,
                255, 255, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[5] + Utils.getPopWindowItems(context, 5)[2] + 0,
                255, 0, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[5] + Utils.getPopWindowItems(context, 5)[2] + 1,
                0, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[5] + Utils.getPopWindowItems(context, 5)[2] + 2,
                0, 0, 255).save();

        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[0] + 0,
                0, 255, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[0] + 1,
                0, 0, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[1] + 0,
                255, 0, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[1] + 1,
                0, 0, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[1] + 2,
                0, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 0,
                255, 255, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 1,
                0, 255, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 2,
                0, 0, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 3,
                255, 0, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 4,
                255, 0, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 5,
                255, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[6] + Utils.getPopWindowItems(context, 6)[2] + 6,
                0, 255, 0).save();

        new RgbColor(DEFAULT_COLORS + itemNames[7] + Utils.getPopWindowItems(context, 7)[0] + 0,
                0, 255, 50).save();
        new RgbColor(DEFAULT_COLORS + itemNames[7] + Utils.getPopWindowItems(context, 7)[0] + 1,
                0, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[7] + Utils.getPopWindowItems(context, 7)[0] + 2,
                255, 255, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[7] + Utils.getPopWindowItems(context, 7)[0] + 3,
                255, 125, 0).save();
        new RgbColor(DEFAULT_COLORS + itemNames[7] + Utils.getPopWindowItems(context, 7)[0] + 4,
                255, 0, 0).save();

        new RgbColor(DEFAULT_COLORS + itemNames[8] + Utils.getPopWindowItems(context, 8)[0] + 0,
                255, 0, 132).save();

        new RgbColor(DEFAULT_COLORS + itemNames[9] + Utils.getPopWindowItems(context, 9)[0] + 0,
                255, 0, 132).save();
        new RgbColor(DEFAULT_COLORS + itemNames[9] + Utils.getPopWindowItems(context, 9)[0] + 1,
                0, 50, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[9] + Utils.getPopWindowItems(context, 9)[0] + 2,
                0, 35, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[9] + Utils.getPopWindowItems(context, 9)[0] + 3,
                0, 50, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[9] + Utils.getPopWindowItems(context, 9)[0] + 4,
                0, 230, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[9] + Utils.getPopWindowItems(context, 9)[0] + 5,
                0, 50, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[9] + Utils.getPopWindowItems(context, 9)[0] + 6,
                0, 35, 255).save();
        new RgbColor(DEFAULT_COLORS + itemNames[9] + Utils.getPopWindowItems(context, 9)[0] + 7,
                0, 50, 255).save();

        new RgbColor(DEFAULT_COLORS + itemNames[10] + Utils.getPopWindowItems(context, 10)[0] + 0,
                245, 208, 20).save();
        new RgbColor(DEFAULT_COLORS + itemNames[10] + Utils.getPopWindowItems(context, 10)[0] + 1,
                245, 122, 20).save();
        new RgbColor(DEFAULT_COLORS + itemNames[10] + Utils.getPopWindowItems(context, 10)[0] + 2,
                245, 208, 20).save();
        new RgbColor(DEFAULT_COLORS + itemNames[10] + Utils.getPopWindowItems(context, 10)[0] + 3,
                245, 122, 20).save();

        saveDefaultColors();
    }

    public static void saveDefaultColors() {
        new RgbColor(DEFAULT_COLORS + 0, 255, 255, 255).save(); //view 1
        new RgbColor(DEFAULT_COLORS + 1, 0, 255, 255).save();//view 2
        new RgbColor(DEFAULT_COLORS + 2, 0, 0, 255).save();//view 3
        new RgbColor(DEFAULT_COLORS + 3, 255, 0, 255).save();//view 4
        new RgbColor(DEFAULT_COLORS + 4, 255, 0, 0).save();//view 5
        new RgbColor(DEFAULT_COLORS + 5, 255, 255, 0).save();//view 6
        new RgbColor(DEFAULT_COLORS + 6, 0, 255, 0).save();//view 7
        new RgbColor(DEFAULT_COLORS + 7, 0, 255, 0).save();//view 8
    }

    public static List<RgbColor> getDefaultColors(String name, int position) {
        List<RgbColor> rgbColorList = RgbColor.getRgbColorList(DEFAULT_COLORS + name + position);
        if (rgbColorList == null || rgbColorList.size() == 0) {
            rgbColorList = RgbColor.getRgbColorList(DEFAULT_COLORS + position);
        }
        return rgbColorList;
    }

}
