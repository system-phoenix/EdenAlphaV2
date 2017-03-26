package com.systemphoenix.edenalpha.Codex;

import com.badlogic.gdx.math.Vector2;

public class EnemyCodex {

    public static int getHP(int index, int wave, int regionIndex) {
        return HP[index] + ((wave + 1) * regionIndex) * 10;
    }

    public static final int HP[] = {
            300,    450,    600,    150,     750,    450,    900,    1200
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

    public static final float seedDrop[] = {
            0.25f,  0.5f,   0.75f,  1,      1.25f,  1.5f,   1.75f,  2
    };

    public static Vector2[] getSpriteLocation(int index) {
        Vector2[] temp = {new Vector2(((index % 4) + ((index % 4) * 3)), (index / 4) * 4), new Vector2(((index % 4 + 3) + ((index % 4) * 3)), ((index / 4) * 4 + 3))};
        return temp;
    }
}
