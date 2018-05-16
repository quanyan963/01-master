package com.example.txtledbluetooth.bean;

import com.example.txtledbluetooth.utils.Utils;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KomoriWu
 * on 2017-05-12.
 */
@Table(name = "light_type")
public class LightType extends SugarRecord implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("speed")
    private int speed;
    @SerializedName("brightness")
    private int brightness;
    @SerializedName("popupPosition")
    private int popupPosition;
    @SerializedName("isOpen")
    private boolean isOpen;

    public LightType() {
    }


    public LightType(String name, int speed, int brightness, int popupPosition, boolean isOpen) {
        this.name = name;
        this.speed = speed;
        this.brightness = brightness;
        this.popupPosition = popupPosition;
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }


    public int getPopupPosition() {
        return popupPosition;
    }

    public void setPopupPosition(int popupPosition) {
        this.popupPosition = popupPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<LightType> getLightTypeList(String name) {
        return LightType.find(LightType.class, "name = ?", name);
    }

    public void deleteLightTypeByName() {
        LightType.deleteAll(LightType.class, "name = ?", name);
    }

    public static void deleteLightTypeByName(String name) {
        LightType.deleteAll(LightType.class, "name = ?", name);
    }

    public static HashMap<String, Integer> getSbProgressMap(String name, int position) {
        List<LightType> sbProgressList = getLightTypeList(name);
        HashMap<String, Integer> hashMap = new HashMap<>();
        if (sbProgressList == null || sbProgressList.size() == 0) {
            hashMap = Utils.getSeekBarProgress(position);
        } else {
            hashMap.put(Utils.SEEK_BAR_PROGRESS_BRIGHT, sbProgressList.get(0).getBrightness());
            hashMap.put(Utils.SEEK_BAR_PROGRESS_SPEED, sbProgressList.get(0).getSpeed());
        }
        return hashMap;
    }

    public static boolean getPulseIsOpen(String name) {
        List<LightType> lightTypeList = getLightTypeList(name);
        if (lightTypeList != null &&lightTypeList.size() > 0) {
            return lightTypeList.get(0).isOpen();
        }
        return false;
    }
}
