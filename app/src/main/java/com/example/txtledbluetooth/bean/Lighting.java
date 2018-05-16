package com.example.txtledbluetooth.bean;

/**
 * Created by KomoriWu
 * on 2017-04-22.
 */

public class Lighting {

    private String lightingName;
    private int lightingIcon;
    private boolean isEdit;

    public Lighting(String lightingName, int lightingIcon, boolean isEdit) {
        this.lightingName = lightingName;
        this.lightingIcon = lightingIcon;
        this.isEdit = isEdit;
    }

    public String getLightingName() {
        return lightingName;
    }

    public void setLightingName(String lightingName) {
        this.lightingName = lightingName;
    }

    public int getLightingIcon() {
        return lightingIcon;
    }

    public void setLightingIcon(int lightingIcon) {
        this.lightingIcon = lightingIcon;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
