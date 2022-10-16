package com.trikzon.transparent;

import com.google.gson.annotations.SerializedName;

public class ConfigBean {
    @SerializedName("beacon_beam")
    public boolean beaconBeam = false;
    @SerializedName("end_crystal")
    public boolean endCrystal = true;
    @SerializedName("item_frame")
    public boolean itemFrame = true;
    public boolean painting = true;

    public static ConfigBean empty() {
        var result = new ConfigBean();
        result.beaconBeam = false;
        result.endCrystal = false;
        result.itemFrame = false;
        result.painting = false;
        return result;
    }

    public void or(ConfigBean other) {
        this.beaconBeam |= other.beaconBeam;
        this.endCrystal |= other.endCrystal;
        this.itemFrame |= other.itemFrame;
        this.painting |= other.painting;
    }
}
