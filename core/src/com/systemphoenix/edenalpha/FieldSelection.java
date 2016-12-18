package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.MathUtils;

public class FieldSelection {

    private Texture[] phMaps;
    private Sprite[] mapSprites;
    private Region[] regions;

    private String[] codes, names;
    private float[] coordsX, coordsY, forestLandPercentages;
    private int[] regionIndeces;

    private boolean flinging = false, xAligned = false, yAligned = false, firstCall = true;
    private int index = 0, pastIndex = -1, counter = 0, padding = 20, spaces = 15;
    private float velX, velY;
    private GlyphLayout glyphLayout;
    private BitmapFont font;
    private Preferences levels;

    public FieldSelection(int index) {
        this.index = index;
        this.font = new BitmapFont();
        this.font.getData().setScale(1f);
        this.glyphLayout = new GlyphLayout();
        this.levels = Gdx.app.getPreferences("All Levels");
        this.codes = new String[17];    this.names = new String[17];
        this.coordsX = new float[17];   this.coordsY = new float[17];
        this.forestLandPercentages = new float[17];
        this.regionIndeces = new int[17];
        try{
            for(int i = 0; i < codes.length; i++) {
                codes[i] = levels.getString("code" + i);
                names[i] = levels.getString("name" + i);
                coordsX[i] = levels.getFloat("coordsX" + i);
                coordsY[i] = levels.getFloat("coordsY" + i);
                forestLandPercentages[i] = levels.getFloat("forestLandPercentage" + i);
                regionIndeces[i] = levels.getInteger("regionIndex" + i);
            }
        } catch (Exception e) {
            codes[0]    = "CAR";                    coordsX[0]  = 685f;         coordsY[0]  = 1075f;
            codes[1]    = "Region I";               coordsX[1]  = 645f;         coordsY[1]  = 1045f;
            codes[2]    = "Region II";              coordsX[2]  = 745f;         coordsY[2]  = 1095f;
            codes[3]    = "Region III";             coordsX[3]  = 675f;         coordsY[3]  = 935f;
            codes[4]    = "Region IVA";             coordsX[4]  = 745f;         coordsY[4]  = 805f;
            codes[5]    = "Region IVB";             coordsX[5]  = 645f;         coordsY[5]  = 675f;
            codes[6]    = "Region V";               coordsX[6]  = 875f;         coordsY[6]  = 725f;
            codes[7]    = "Region VI";              coordsX[7]  = 825f;         coordsY[7]  = 535f;
            codes[8]    = "Region VII";             coordsX[8]  = 885f;         coordsY[8]  = 465f;
            codes[9]    = "Region VIII";            coordsX[9]  = 995f;         coordsY[9]  = 565f;
            codes[10]   = "Region IX";              coordsX[10] = 835f;         coordsY[10] = 275f;
            codes[11]   = "Region X";               coordsX[11] = 965f;         coordsY[11] = 325f;
            codes[12]   = "Region XI";              coordsX[12] = 1065f;        coordsY[12] = 225f;
            codes[13]   = "Region XII";             coordsX[13] = 985f;         coordsY[13] = 195f;
            codes[14]   = "Region XIII";            coordsX[14] = 1045f;        coordsY[14] = 355f;
            codes[15]   = "ARMM";                   coordsX[15] = 795f;         coordsY[15] = 195f;
            codes[16]   = "NCR";                    coordsX[16] = 665f;         coordsY[16] = 835f;

            names[0]      = "Cordillera Administrative Region";     forestLandPercentages[0]  = 81;    regionIndeces[0]  = 0;
            names[1]      = "Ilocos Region";                        forestLandPercentages[1]  = 37;    regionIndeces[1]  = 1;
            names[2]      = "Cagayan Valley";                       forestLandPercentages[2]  = 64;    regionIndeces[2]  = 2;
            names[3]      = "Central Luzon";                        forestLandPercentages[3]  = 44;    regionIndeces[3]  = 3;
            names[4]      = "CALABARZON";                           forestLandPercentages[4]  = 35;    regionIndeces[4]  = 4;
            names[5]      = "MIMAROPA";                             forestLandPercentages[5]  = 64;    regionIndeces[5]  = 5;
            names[6]      = "Bicol Region";                         forestLandPercentages[6]  = 31;    regionIndeces[6]  = 6;
            names[7]      = "Western Visayas";                      forestLandPercentages[7]  = 30;    regionIndeces[7]  = 7;
            names[8]      = "Central Visayas";                      forestLandPercentages[8]  = 35;    regionIndeces[8]  = 8;
            names[9]      = "Eastern Visayas";                      forestLandPercentages[9]  = 52;    regionIndeces[9]  = 9;
            names[10]     = "Zamboangan Peninsula";                 forestLandPercentages[10] = 54;    regionIndeces[10] = 10;
            names[11]     = "Northern Mindanao";                    forestLandPercentages[11] = 52;    regionIndeces[11] = 11;
            names[12]     = "Davao Region";                         forestLandPercentages[12] = 63;    regionIndeces[12] = 12;
            names[13]     = "SOCCSKSARGEN";                         forestLandPercentages[13] = 61;    regionIndeces[13] = 13;
            names[14]     = "Caraga";                               forestLandPercentages[14] = 71;    regionIndeces[14] = 14;
            names[15]     = "Autonomous Region in Muslim Mindanao"; forestLandPercentages[15] = 51;    regionIndeces[15] = 15;
            names[16]     = "National Capital Region";              forestLandPercentages[16] = 24;    regionIndeces[16] = 16;
            
            for(int i = 0; i < codes.length; i++) {
                levels.putString("code" + i, codes[i]);
                levels.putString("name" + i, names[i]);
                levels.putFloat("coordsX" + i, coordsX[i]);
                levels.putFloat("coordsY" + i, coordsY[i]);
                levels.putFloat("forestLandPercentage" + i, forestLandPercentages[i]);
                levels.putInteger("regionIndex" + i, regionIndeces[i]);
            }
        }

        createRegions();

        phMaps = new Texture[17];
        mapSprites = new Sprite[17];
        regions = new Region[17];
        for (int i = 0; i < phMaps.length; i++) {
            phMaps[i] = new Texture("[PH]map" + i + ".png");
            mapSprites[i] = new Sprite(phMaps[i]);
        }
    }

    private void createRegions() {
        for(int i = 0; i < codes.length; i++) {
            regions[i] = new Region(codes[i], names[i], forestLandPercentages[i], coordsX[i], coordsY[i], regionIndeces[i]);
        }
    }

    public void render(SpriteBatch gameGraphics, OrthographicCamera cam) {
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
