package com.trikzon.transparent;

public class ConfigBean {
    public boolean painting = true;
    public boolean item_frame = true;
    public boolean beacon_beam = false;

    public void or(ConfigBean other) {
        painting = painting || other.painting;
        item_frame = item_frame || other.item_frame;
        beacon_beam = beacon_beam || other.beacon_beam;
    }
}
