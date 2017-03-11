package com.systemphoenix.edenalpha.Codex;

import com.badlogic.gdx.math.Vector2;

public class EnemyCodex {

    public static int getHP(int index, int wave, int regionIndex) {
        return HP[index] + (wave + 1) * regionIndex;
    }

    public static final int HP[] = {
            100,    150,    200,    50,     250,    150,    300,    400
    };

    public static final float damage[] = {
            0,      0,      0,      0,      0,      0,      0,      0
    };

    public static final float speed[] = {
            25,     35,     40,     15,     50,     40,     45,     50
    };

    public static Vector2[] getSpriteLocation(int index) {
        Vector2[] temp = {new Vector2(((index % 4) + ((index % 4) * 3)), (index / 4) * 4), new Vector2(((index % 4 + 3) + ((index % 4) * 3)), ((index / 4) * 4 + 3))};
        return temp;
    }
}
