package com.systemphoenix.edenalpha.Codex;

import com.badlogic.gdx.math.Vector2;

public class EnemyCodex {

    public static final float baseHP = 500, baseDmg = 50, baseAS = 1000, baseMS = 50;

    public static final String name[] = {
            "Explorer",
            "Commercial Farmer",
            "Rancher",
            "Scientist",
            "Miner",
            "Kaingin Farmer",
            "Illegal Logger",
            "Businessman"
    };

    public static float getHP(int index, int wave, int regionIndex) {
//        return HP[index] + (int)(Math.pow((double)wave, 2)) + (wave * 10 * regionIndex);
        return HP[index] + (25 * (wave + 1));
    }

    public static final int HP[] = {
//            300,    450,    600,    1500,     750,    450,    900,    1200
            25,    38,    50,    125,    63,    38,    75,    100
    };

    public static final float damage[] = {
            5,      5,      15,     30,     25,     45,     30,     50
    };

    public static final float speed[] = {
            25,     35,     40,     15,     50,     40,     45,     50
    };

    public static final long attackSpeed[] = {
            1000,   1000,   1500,   3000,   750,    500,    1500,   1000
    };

    public static final float waterDrop[] = {
            1,      2,      3,      4,      5,      6,      7,      8
    };

    public static final String description[][] = {
            {
                    "",
                    "",
                    "",
                    "",
                    "",
            }, {
                    "",
                    "",
                    "",
                    "",
                    "",
            }, {
                    "",
                    "",
                    "",
                    "",
                    "",
            }, {
                    "",
                    "",
                    "",
                    "",
                    "",
            }, {
                    "",
                    "",
                    "",
                    "",
                    "",
            }, {
                    "",
                    "",
                    "",
                    "",
                    "",
            }, {
                    "",
                    "",
                    "",
                    "",
                    "",
            }, {
                    "",
                    "",
                    "",
                    "",
                    "",
            }
    };

    public static Vector2[] getSpriteLocation(int index) {
        Vector2[] temp = {new Vector2(((index % 4) + ((index % 4) * 3)), (index / 4) * 4), new Vector2(((index % 4 + 3) + ((index % 4) * 3)), ((index / 4) * 4 + 3))};
        return temp;
    }
}
