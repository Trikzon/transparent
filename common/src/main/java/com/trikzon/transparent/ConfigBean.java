package com.trikzon.transparent;

import com.google.gson.annotations.SerializedName;

public class ConfigBean {
    public boolean painting = true;
    @SerializedName("item_frame")
    public boolean itemFrame = true;
    @SerializedName("beacon_beam")
    public boolean beaconBeam = false;

    public static ConfigBean empty() {
        ConfigBean result = new ConfigBean();
        result.painting = false;
        result.itemFrame = false;
        result.beaconBeam = false;
        return result;
    }

    public void or(ConfigBean other) {
        this.painting |= other.painting;
        this.itemFrame |= other.itemFrame;
        this.beaconBeam |= other.beaconBeam;
    }
}
