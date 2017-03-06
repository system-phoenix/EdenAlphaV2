package com.systemphoenix.edenalpha.Codex;

import com.badlogic.gdx.math.Vector2;

public class PlantCodex {

    public static final float baseHP = 500;
    public static final long baseAS = 1000;
    public static final Vector2 baseDmg = new Vector2(45, 50);

    public static final String plantName[]= {
            "Pine",     "Mango",    "Coconut",  "Mushroom",     "Rafflesia",    "Mangrove",     "Acacia",   "Anahaw",   "Santol",   "Duhat",    "Kapok",    "Bamboo",   "Narra",    "Balete",   "Pili"
    };

    public static final short typeBit[] = {
            1,          1,          1 | 2,      1 | 4,          1,              2,              1,           1,         1,          1,          1,          1 | 4,      1,          1,          1
    };

    public static final float cost[] = {
            50,         25,         100,        15,             100,            75,             150,         120,       200,        150,        350,        200,        300,        150,        350
    };

    public static final float maxHP[] = {
            200,        100,        50,         10,             50,             25,             100,         25,        80,         150,        baseHP,        10,         300,        250,        100
    };

    public static final long AS[] = {
            1500,       baseAS,       2000,       1500,           baseAS,           500,            750,         750,       baseAS,       333,        333,        3000,       500,        0,          500,
    };

    public static final boolean target[] = {
            true,       true,       false,      true,           false,          true,           true,        true,      true,       true,       false,      false,      true,       true,       true
    };

    public static final float range[] = {
            2.5f,       2,          1,          1,              1,              1.5f,           2,           2,         3,          2.5f,       5,          3,          3,          1,          3
    };

    public static final float effectiveRange[] = {
            2.5f,       1,          0,          0,              2,              0,              2,           0,         2.5f,       1,          4,          0,          2,          1,          1
    };

    public static final float growthTime[] = {
            35,         15,         30,         5,              10,             20,             15,          25,        20,         15,         60,         45,         30,         15,         45
    };

    public static final int level[] = {
            0,          0,          1,          3,              4,              5,              6,           7,         8,          9,          10,         11,         12,         13,         14
    };

    public static final float seedProduction[] = {
            1.5f,       5,          1,          0,              0,              0,              2.5f,        0,         0,          0,          20,         0,          10,         0,          5
    };

    public static final float projectileSize[] = {
            8,          16,         32,         8,              0,              0,              16,          32,        16,         8,          16,         0,          8,          0,          16
    };

    public static final Vector2[] DMG = {
            new Vector2(25, 35),    new Vector2(25, 35),    new Vector2(35, 40),    new Vector2(0, 5),    new Vector2(15, 20),    new Vector2(15, 20),    new Vector2(20, 25),    new Vector2(25, 35),    new Vector2(35, 40),    new Vector2(0, 5),    new Vector2(0, 5),    new Vector2(45, 50),    new Vector2(15, 20),    new Vector2(0, 0),  new Vector2(35, 40)
    };

    public static final float[] plantSelectorIndex = {
            368f, 464f, 560f, 656f, 752f, 848f
    };

}
