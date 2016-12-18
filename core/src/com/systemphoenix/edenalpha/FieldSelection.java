package com.systemphoenix.edenalpha;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class FieldSelection {

    private Texture[] phMaps;
    private Sprite[] mapSprites;
    private Region[] regions;

    private boolean flinging = false, xAligned = false, yAligned = false, firstCall = true;
    private int index = 0, counter = 0;
    private float velX, velY;
    private BitmapFont font;

    public FieldSelection(int index) {
        this.index = index;
        this.font = new BitmapFont();
        this.font.getData().setScale(1f);

        phMaps = new Texture[17];
        mapSprites = new Sprite[17];
        regions = new Region[17];
        for (int i = 0; i < phMaps.length; i++) {
            phMaps[i] = new Texture("[PH]map" + i + ".png");
            mapSprites[i] = new Sprite(phMaps[i]);
        }

        createRegions();
    }

    private void createRegions() {
        regions[0]  = new Region("CAR",         "Cordillera Administrative Region",     81, 685f,  1057f, 0);
        regions[1]  = new Region("Region I",    "Ilocos",                               37, 645f,  1045f, 1);
        regions[2]  = new Region("Region II",   "Cagayan Valley",                       64, 745f,  1095f, 2);
        regions[3]  = new Region("Region III",  "Central Luzon",                        44, 675f,  935f,  3);
        regions[4]  = new Region("Region IVA",  "CALABARZON",                           35, 745f,  805f,  4);
        regions[5]  = new Region("Region IVB",  "MIMAROPA",                             64, 645f,  675f,  5);
        regions[6]  = new Region("Region V",    "Bicol",                                31, 875f,  725f,  6);
        regions[7]  = new Region("Region VI",   "Western Visayas",                      30, 825f,  535f,  7);
        regions[8]  = new Region("Region VII",  "Central Visayas",                      35, 885f,  465f,  8);
        regions[9]  = new Region("Region VIII", "Eastern Visayas",                      52, 995f,  565f,  9);
        regions[10] = new Region("Region IX",   "Zamboanga Peninsula",                  54, 835f,  275f,  10);
        regions[11] = new Region("Region X",    "Northern Mindanao",                    52, 965f,  325f,  11);
        regions[12] = new Region("Region XI",   "Davao",                                63, 1065f, 225f,  12);
        regions[13] = new Region("Region XII",  "SOCCSKSARGEN",                         61, 985f,  195f,  13);
        regions[14] = new Region("Region XIII", "Caraga",                               71, 1045f, 355f,  14);
        regions[15] = new Region("ARMM",        "Autonomous Region in Muslim Mindanao", 51, 795f,  195f,  15);
        regions[16] = new Region("NCR",         "National Capital Region",              24, 665f,  835f,  16);
    }

    public void render(SpriteBatch gameGraphics, OrthographicCamera cam) {
        int padding = 20, spaces = 15;
        mapSprites[regions[index].getMapIndex()].draw(gameGraphics);
        font.draw(gameGraphics, regions[index].getCode(), cam.position.x - cam.viewportWidth / 2 + padding, cam.position.y + cam.viewportHeight / 2 - padding);
        font.draw(gameGraphics, regions[index].getName(), cam.position.x - cam.viewportWidth / 2 + padding, cam.position.y + cam.viewportHeight / 2 - padding - spaces);
//        font.getData().setScale(1.5f);
        font.draw(gameGraphics, "Forest land percentage: " + (regions[index].getLifePercentage()), cam.position.x - cam.viewportWidth / 2 + padding, cam.position.y + cam.viewportHeight / 2 - padding - (spaces) * 2);
//        font.getData().setScale(1f);
    }

    public void setCameraPosition(OrthographicCamera cam) {
        float x, y;
        y = MathUtils.round(cam.position.y);
        x = MathUtils.round(cam.position.x);
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
        cam.position.set(x, y, 0);
    }

    public void setIndex(int update) {
        if(!flinging) {
            int pastIndex = index;
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

    public Region getRegionByIndex(int i) {
        return regions[i];
    }

    public void dispose() {
        for(int i = 0; i < phMaps.length; i++) {
            phMaps[i].dispose();
        }
    }

}
