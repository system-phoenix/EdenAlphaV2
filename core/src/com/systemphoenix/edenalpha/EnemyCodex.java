package com.systemphoenix.edenalpha;

import com.badlogic.gdx.math.Vector2;

public class EnemyCodex {

    public static int getHP(int index) {
        return index * 2 + 1;
    }

    public static Vector2[] getSpriteLocation(int index) {
        Vector2[] temp = {new Vector2(index % 4, index / 4), new Vector2(index % 4 + 3, index / 4 + 3)};
        return temp;
    }
}
