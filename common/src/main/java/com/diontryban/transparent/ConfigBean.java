/*
 * This file is part of Transparent. A copy of this program can be found at
 * https://github.com/Trikzon/transparent.
 * Copyright (C) 2023 Dion Tryban
 *
 * Transparent is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Transparent is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Transparent. If not, see <https://www.gnu.org/licenses/>.
 */

package com.diontryban.transparent;

import com.google.gson.annotations.SerializedName;

public class ConfigBean {
    @SerializedName("armor_stand")
    public boolean armorStand = true;
    @SerializedName("beacon_beam")
    public boolean beaconBeam = false;
    @SerializedName("end_crystal")
    public boolean endCrystal = true;
    @SerializedName("item_frame")
    public boolean itemFrame = true;
    public boolean painting = true;

    public static ConfigBean empty() {
        var result = new ConfigBean();
        result.armorStand = false;
        result.beaconBeam = false;
        result.endCrystal = false;
        result.itemFrame = false;
        result.painting = false;
        return result;
    }

    public void or(ConfigBean other) {
        this.armorStand |= other.armorStand;
        this.beaconBeam |= other.beaconBeam;
        this.endCrystal |= other.endCrystal;
        this.itemFrame |= other.itemFrame;
        this.painting |= other.painting;
    }
}
