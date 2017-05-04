package com.systemphoenix.edenalpha.WindowUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.systemphoenix.edenalpha.Screens.MapScreen;

public class FieldSelection {

    private MapScreen mapScreen;

    private Texture[] phMaps;
    private Sprite[] mapSprites;
    private Region[] regions;

    private boolean flinging = false, xAligned = false, yAligned = false, firstCall = true;
    private int index = 0, counter = 0, lowLevelBound, highLevelBound;
    private float velX, velY;

    public FieldSelection(MapScreen mapScreen, int index, int lowLevelBound, int highLevelBound) {
        this.index = index;
        this.mapScreen = mapScreen;
        this.lowLevelBound = lowLevelBound;
        this.highLevelBound = highLevelBound;

        phMaps = new Texture[17];
        mapSprites = new Sprite[17];
        regions = new Region[17];
        try {
            for (int i = 0; i < phMaps.length; i++) {
                phMaps[i] = new Texture(Gdx.files.internal("maps/[PH]map" + i + ".png"));
                mapSprites[i] = new Sprite(phMaps[i]);
            }
            Gdx.app.log("Verbose", "Successfully loaded maps.");
        } catch(Exception e) {
            Gdx.app.log("Verbose", "maps " + e.getMessage());
        }

        createRegions();
    }

    private void createRegions() {
        for(int i = 0; i < regions.length; i++) {
            regions[i] = new Region(i);
        }
    }

    public void render(SpriteBatch gameGraphics) {
        mapSprites[regions[index].getMapIndex()].draw(gameGraphics);
        this.highLevelBound = mapScreen.getHighLevelBound();
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
        }
        cam.position.set(x, y, 0);
    }

    public void setIndex(int update) {
        if(!flinging) {
            int pastIndex = index;
            if(!mapScreen.getGame().isDevMode()) {
                index += (update * 2);
                if(lowLevelBound == 1 && update < 0 && index % 2 == 0) {
                    index++;
                }
            } else {
                index += update;
            }
            if (index < lowLevelBound) {
                index = lowLevelBound;
            } else if (index > 16) {
                index = 16 ;
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

    public Region getRegion() {
        return regions[index];
    }

    public Region[] getRegions() {
        return regions;
    }

    public void dispose() {
        for(int i = 0; i < phMaps.length; i++) {
            phMaps[i].dispose();
        }
    }

}
