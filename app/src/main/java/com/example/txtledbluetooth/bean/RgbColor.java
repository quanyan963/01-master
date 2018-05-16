package com.example.txtledbluetooth.bean;

import android.graphics.Color;

import com.example.txtledbluetooth.utils.SqlUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KomoriWu
 * on 2017-05-12.
 */
@Table(name = "rgb_color")
public class RgbColor extends SugarRecord implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("r")
    private int r;
    @SerializedName("g")
    private int g;
    @SerializedName("b")
    private int b;
    @SerializedName("colorInt")
    private int colorInt;
    @SerializedName("colorStr")
    private String colorStr;

    public RgbColor() {
    }

    public RgbColor(String name, int r, int g, int b) {
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.colorInt = Color.rgb(r, g, b);
        this.colorStr = getBothColor(r) + getBothColor(g) + getBothColor(b);
    }


    public String getBothColor(int str) {
        if (Integer.toString(str).getBytes().length < 2) {
            return "0" + Integer.toHexString(str);
        } else {
            return Integer.toHexString(str);
        }

    }

    public int getColorInt() {
        return colorInt;
    }

    public void setColorInt(int colorInt) {
        this.colorInt = colorInt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public String getColorStr() {
        return colorStr;
    }

    public void setColorStr(String colorStr) {
        this.colorStr = colorStr;
    }

    public static List<RgbColor> getRgbColorList(String name) {
        return RgbColor.find(RgbColor.class, "name = ?", name);
    }

    public static int[] getRgbColors(String name) {
        int[] colors = new int[8];
        for (int i = 0; i < 8; i++) {
            List<RgbColor> rgbColorList = getRgbColorList(name + i);
            if (rgbColorList == null || rgbColorList.size() == 0) {
                rgbColorList = SqlUtils.getDefaultColors(name, i);
            }
            if (rgbColorList.size() > 0) {
                colors[i] = rgbColorList.get(0).getColorInt();
            }
        }

        return colors;
    }

    public static String[] getRgbColorStr(String name) {
        String[] colors = new String[8];
        for (int i = 0; i < 8; i++) {
            List<RgbColor> rgbColorList = getRgbColorList(name + i);
            if (rgbColorList == null || rgbColorList.size() == 0) {
                rgbColorList = SqlUtils.getDefaultColors(name, i);
            }
            if (rgbColorList.size() > 0) {
                colors[i] = rgbColorList.get(0).getColorStr();
            }
        }

        return colors;
    }

    public void deleteRgbColorByName() {
        RgbColor.deleteAll(RgbColor.class, "name = ?", name);
    }

    public static void deleteRgbColors(String name) {
        for (int i = 0; i < 8; i++) {
            RgbColor.deleteAll(RgbColor.class, "name = ?", name + i);
        }
    }


}
