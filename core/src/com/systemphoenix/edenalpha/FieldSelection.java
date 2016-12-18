package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class FieldSelection {

    private Texture[] phMaps;
    private Sprite[] mapSprites;
    private Region[] regions;

    private boolean flinging = false, xAligned = false, yAligned = false, firstCall = true;
    private int index = 0, pastIndex = -1, counter = 0;
    private float velX, velY;

    public FieldSelection(int index) {
        this.index = index;
        phMaps = new Texture[17];
        mapSprites = new Sprite[17];
        regions = new Region[17];
        for (int i = 0; i < phMaps.length; i++) {
            phMaps[i] = new Texture("[PH]map" + i + ".png");
            mapSprites[i] = new Sprite(phMaps[i]);
        }

        regions[0]      = new Region("CAR - Cordillera Administrative Region",      685f,    1075f,    0);
        regions[1]      = new Region("Region I - Ilocos Region",                    645f,    1045f,    1);
        regions[2]      = new Region("Region II - Cagayan Valley",                  745f,    1095f,    2);
        regions[3]      = new Region("Region III - Central Luzon",                  675f,    935f,     3);
        regions[4]      = new Region("Region IVA - CALABARZON",                     745f,    805f,     4);
        regions[5]      = new Region("Region IVB - MIMAROPA",                       645f,    675f,     5);
        regions[6]      = new Region("Region V - Bicol Region",                     875f,    725f,     6);
        regions[7]      = new Region("Region VI - Western Visayas",                 825f,    535f,     7);
        regions[8]      = new Region("Region VII - Central Visayas",                885f,    465f,     8);
        regions[9]      = new Region("Region VIII - Eastern Visayas",               995f,    565f,     9);
        regions[10]     = new Region("Region IX - Zamboangan Peninsula",            835f,    275f,    10);
        regions[11]     = new Region("Region X - Northern Mindanao",                965f,    325f,    11);
        regions[12]     = new Region("Region XI - Davao Region",                    1065f,   225f,    12);
        regions[13]     = new Region("Region XII - SOCCSKSARGEN",                   985f,    195f,    13);
        regions[14]     = new Region("Region XIII - Caraga",                        1045f,   355f,    14);
        regions[15]     = new Region("ARMM - Autonomous Region in Muslim Mindanao", 795f,    195f,    15);
        regions[16]     = new Region("NCR - National Capital Region",               665f,    835f,    16);
    }

    public void render(SpriteBatch gameGraphics) {
        mapSprites[regions[index].getMapIndex()].draw(gameGraphics);
    }

    public void setCameraPosition(OrthographicCamera camera) {
        float x, y;
        y = MathUtils.round(camera.position.y);
        x = MathUtils.round(camera.position.x);
        if(firstCall) {
            x = regions[index].getX();
            y = regions[index].getY();
            firstCall = false;
        } else if(flinging) {
//            Gdx.app.log("Verbose", "[" + counter + "]Current x: " + x + ", target x: " + regions[index].getX() + "\t\tvelX: " + velX);
//            Gdx.app.log("Verbose", "[" + counter + "]Current y: " + y + ", target y: " + regions[index].getY() + "\t\tvelY: " + velY);
            counter++;
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
            if((xAligned && yAligned) || counter > 10) {
                xAligned = yAligned = flinging = false;
                velX = velY = 0;
                counter = 0;
            }
//            Gdx.app.log("Verbose", "xAligned: " + xAligned + ", yAligned: " + yAligned);
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

    public int getIndex() {
        return index;
    }

    public void dispose() {
        for(int i = 0; i < phMaps.length; i++) {
            phMaps[i].dispose();
        }
    }

}
