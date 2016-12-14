package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FieldSelection {

    private Texture[] phMaps;
    private int index = 0;
    private Sprite[] mapSprites;
    private Region[] regions;

    public FieldSelection() {
        phMaps = new Texture[17];
        mapSprites = new Sprite[17];
        regions = new Region[17];
        for (int i = 0; i < phMaps.length; i++) {
            phMaps[i] = new Texture("[PH]map" + i + ".jpg");
            mapSprites[i] = new Sprite(phMaps[i]);
        }

        regions[0]      = new Region("CAR - Cordillera Administrative Region",      465f,    1073.5f,    0);
        regions[1]      = new Region("Region I - Ilocos Region",                    430f,    1039.5f,    1);
        regions[2]      = new Region("Region II - Cagayan Valley",                  538f,    1065.5f,    2);
        regions[3]      = new Region("Region III - Central Luzon",                  434f,    920.5f,     3);
        regions[4]      = new Region("Region IVA - CALABARZON",                     501f,    792.5f,     4);
        regions[5]      = new Region("Region IVB - MIMAROPA",                       464f,    706.5f,     5);
        regions[6]      = new Region("Region V - Bicol Region",                     663f,    730.5f,     6);
        regions[7]      = new Region("Region VI - Western Visayas",                 593f,    534.5f,     7);
        regions[8]      = new Region("Region VII - Central Visayas",                665f,    483.5f,     8);
        regions[9]      = new Region("Region VIII - Eastern Visayas",               774f,    567.5f,     9);
        regions[10]     = new Region("Region IX - Zamboangan Peninsula",            616.5f,  271.5f,    10);
        regions[11]     = new Region("Region X - Northern Mindanao",                765f,    312.5f,    11);
        regions[12]     = new Region("Region XI - Davao Region",                    774f,    248.5f,    12);
        regions[13]     = new Region("Region XII - SOCCSKSARGEN",                   780f,    186f,      13);
        regions[14]     = new Region("Region XIII - Caraga",                        774f,    393f,      14);
        regions[15]     = new Region("ARMM - Autonomous Region in Muslim Mindanao", 628f,    204f,      15);
        regions[16]     = new Region("NCR - National Capital Region",               405f,    832f,      16);
    }

    public void render(SpriteBatch gameGraphics) {
        mapSprites[index].draw(gameGraphics);
    }

    public void setCameraPosition(OrthographicCamera camera) {
        camera.position.set(regions[index].getX(), regions[index].getY(), 0);
    }

    public void setIndex(int update) {
        index += update;
        if(index < 0) {
            index = 0;
        } else if(index >= mapSprites.length) {
            index = mapSprites.length - 1;
        }
    }

}
