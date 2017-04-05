package com.systemphoenix.edenalpha.Codex;

import com.badlogic.gdx.math.Vector2;

public class EnemyCodex {

    public static int getHP(int index, int wave, int regionIndex) {
        return HP[index] + (int)(Math.pow((double)wave, 2)) + (wave * 10 * regionIndex);
    }

    public static final int HP[] = {
            300,    450,    600,    1500,     750,    450,    900,    1200
    };

    public static final float damage[] = {
            5,      10,     15,     50,     25,     25,     30,     30
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

    public static Vector2[] getSpriteLocation(int index) {
        Vector2[] temp = {new Vector2(((index % 4) + ((index % 4) * 3)), (index / 4) * 4), new Vector2(((index % 4 + 3) + ((index % 4) * 3)), ((index / 4) * 4 + 3))};
        return temp;
    }
}
