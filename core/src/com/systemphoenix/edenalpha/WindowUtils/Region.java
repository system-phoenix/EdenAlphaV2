package com.systemphoenix.edenalpha.WindowUtils;

import com.systemphoenix.edenalpha.Codex.RegionCodex;

public class Region {
    private String name, code;
    private float x, y, forestLandPercentage, worldWidth, worldHeight, sunlight;
    private int mapIndex, arraySizeX, arraySizeY, timeStart, waves[][];
    private String musicFX;

    public Region(int codexIndex) {
        this.code                   = RegionCodex.codes[codexIndex];
        this.name                   = RegionCodex.names[codexIndex];
        this.forestLandPercentage   = RegionCodex.forestPercentage[codexIndex];
        this.x                      = RegionCodex.camX[codexIndex];
        this.y                      = RegionCodex.camY[codexIndex];
        this.mapIndex               = RegionCodex.mapIndeces[codexIndex];
        this.timeStart              = RegionCodex.timeStart[codexIndex];
        this.waves                  = RegionCodex.waves[codexIndex];
        this.musicFX                = RegionCodex.mapSound[codexIndex];
        this.sunlight               = RegionCodex.sunlight[codexIndex];
        this.worldWidth             = 1280;
        this.worldHeight            = 720;
        this.arraySizeX             = 40;
        this.arraySizeY             = 23;
    }

    public String getCode() {
        return code;
    }

    public String getName(){
        return name;
    }

    public float getLifePercentage() {
        return forestLandPercentage;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    public float getSunlight() {
        return sunlight;
    }

    public int getMapIndex() {
        return mapIndex;
    }

    public int getArraySizeX() {
        return arraySizeX;
    }

    public int getArraySizeY() {
        return arraySizeY;
    }

    public int getTimeStart() {
        return timeStart;
    }

    public int[][] getWaves() {
        return waves;
    }

    public String getMusicFX() {
        return musicFX;
    }
}
