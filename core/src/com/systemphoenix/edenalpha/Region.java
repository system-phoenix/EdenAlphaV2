package com.systemphoenix.edenalpha;

public class Region {
    private String name, code;
    private float x, y, forestLandPercentage;
    private int mapIndex;

    public Region(String code, String name, float forestLandPercentage, float x, float y, int mapIndex) {
        this.code = code;
        this.name = name;
        this.forestLandPercentage = forestLandPercentage;
        this.x = x;
        this.y = y;
        this.mapIndex = mapIndex;
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

    public int getMapIndex() {
        return mapIndex;
    }
}
