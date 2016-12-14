package com.systemphoenix.edenalpha;

/**
 * Created by System Phoenix on 14/12/2016.
 */
public class Region {
    private String name;
    private float x, y;
    private int mapIndex;

    public Region(String name, float x, float y, int mapIndex) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.mapIndex = mapIndex;
    }

    public String getName(){
        return name;
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
