package com.systemphoenix.edenalpha.Codex;

import com.badlogic.gdx.math.Vector2;

public class PlantCodex {

    public static final int LOWEST = 0;
    public static final int LOWER = 1;
    public static final int LOW = 2;
    public static final int MOD = 3;
    public static final int HIGH = 4;
    public static final int HIGHER = 5;
    public static final int HIGHEST = 6;

    public static final String[] plantNames = {
            "Pine",
            "Mango",
            "Mangrove",
            "Coconut",
            "Bamboo",
            "Narra"
    };

    public static final int[] asStats = {
            LOW,
            MOD,
            HIGHER,
            LOWER,
            LOWEST,
            HIGH
    };

    public static final int[] dmgStats = {
            MOD,
            MOD,
            LOW,
            HIGH,
            HIGHER,
            LOW
    };

    public static final float[] range = {
            2f,
            2.5f,
            1.5f,
            1f,
            3.5f,
            3f
    };

    public static final float[] effectiveRange = {
            1.5f,
            1f,
            0f,
            0f,
            1f,
            4f
    };

    public static final long[] AS = {
            2000,
            1750,
            1500,
            1000,
            750,
            500,
            333
    };

    public static final Vector2[] DMG = {
            new Vector2(10, 15),
            new Vector2(15, 20),
            new Vector2(20, 25),
            new Vector2(25, 35),
            new Vector2(35, 40),
            new Vector2(40, 45),
            new Vector2(45, 50)
    };

    public static final float[] plantSelectorIndex = {
            368f, 464f, 560f, 656f, 752f, 848f
    };

}
