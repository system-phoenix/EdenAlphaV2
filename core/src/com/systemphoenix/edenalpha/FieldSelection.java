package com.systemphoenix.edenalpha;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FieldSelection {

    private Texture[] phMaps;
    private Sprite[] mapSprites;
    private Region[] regions;

    private boolean flinging = false, xAligned = false, yAligned = false, firstCall = true;
    private int index = 0, pastIndex = -1;
    private float velX, velY;

    public FieldSelection() {
        phMaps = new Texture[17];
        mapSprites = new Sprite[17];
        regions = new Region[17];
        for (int i = 0; i < phMaps.length; i++) {
            phMaps[i] = new Texture("[PH]map" + i + ".jpg");
            mapSprites[i] = new Sprite(phMaps[i]);
        }

        regions[0]      = new Region("CAR - Cordillera Administrative Region",      465f,    1074f,    0);
        regions[1]      = new Region("Region I - Ilocos Region",                    430f,    1039f,    1);
        regions[2]      = new Region("Region II - Cagayan Valley",                  538f,    1067f,    2);
        regions[3]      = new Region("Region III - Central Luzon",                  434f,    920f,     3);
        regions[4]      = new Region("Region IVA - CALABARZON",                     501f,    792f,     4);
        regions[5]      = new Region("Region IVB - MIMAROPA",                       464f,    706f,     5);
        regions[6]      = new Region("Region V - Bicol Region",                     662f,    736f,     6);
        regions[7]      = new Region("Region VI - Western Visayas",                 593f,    534f,     7);
        regions[8]      = new Region("Region VII - Central Visayas",                665f,    483f,     8);
        regions[9]      = new Region("Region VIII - Eastern Visayas",               774f,    567f,     9);
        regions[10]     = new Region("Region IX - Zamboangan Peninsula",            616f,    271f,    10);
        regions[11]     = new Region("Region X - Northern Mindanao",                765f,    312f,    11);
        regions[12]     = new Region("Region XI - Davao Region",                    774f,    248f,    12);
        regions[13]     = new Region("Region XII - SOCCSKSARGEN",                   774f,    190f,    13);
        regions[14]     = new Region("Region XIII - Caraga",                        774f,    393f,    14);
        regions[15]     = new Region("ARMM - Autonomous Region in Muslim Mindanao", 628f,    204f,    15);
        regions[16]     = new Region("NCR - National Capital Region",               405f,    832f,    16);
    }

    public void render(SpriteBatch gameGraphics) {
        mapSprites[regions[index].getMapIndex()].draw(gameGraphics);
    }

    public void setCameraPosition(OrthographicCamera camera) {
        float x, y;
        x = camera.position.x;
        y = camera.position.y;

        if(firstCall) {
            x = regions[index].getX();
            y = regions[index].getY();
            firstCall = false;
        } else if(flinging) {
            if(Math.round(x) != regions[index].getX()) {
                x += velX;
            } else {
                xAligned = true;
            }
            if(Math.round(y) != regions[index].getY()) {
                y += velY;
            } else {
                yAligned = true;
            }
            if(xAligned && yAligned) {
                xAligned = yAligned = flinging = false;
                velX = velY = 0;
            }
        }
        camera.position.set(x, y, 0);
    }

    public void setIndex(int update) {
        if(!flinging) {
            pastIndex = index;
            index += update;
            if (index < 0) {
                index = 0;
            } else if (index >= mapSprites.length) {
                index = mapSprites.length - 1;
            }

            this.velX = (regions[index].getX() - regions[pastIndex].getX()) / 10;
            this.velY = (regions[index].getY() - regions[pastIndex].getY()) / 10;
            flinging = true;
        }
    }

}
