package com.systemphoenix.edenalpha.Codex;

import com.badlogic.gdx.math.Vector2;

public class EnemyCodex {

    public static final float baseHP = 100, baseDmg = 50, baseAS = 1000, baseMS = 50;

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
        int multiplier = regionIndex % 2 == 0 ? regionIndex + 1 : regionIndex;
        return (HP[index] * multiplier) + (25 * (wave + 1));
    }

    public static final int HP[] = {
//            300,    450,    600,    1500,     750,    450,    900,    1200
            // 25,    38,    50,    125,    63,    38,    75,    100
            20,     20,     100,    100,     30,    30,     75,     75
    };

    public static final float damage[] = {
            5,      5,      15,     15,     30,     30,     50,     50
    };

    public static final float speed[] = {
            25,     25,     15,     15,     40,     40,     50,     50
    };

    public static final long attackSpeed[] = {
            1000,   1000,   3000,   3000,   750,    750,    1500,   1500
    };

    public static final float waterDrop[] = {
            2,      2,      4,      4,      6,      6,      8,      8
    };

    public static final String description[][] = {
            {
                    "Random person that occasionally",
                    "visits the forests. Leaves trash",
                    "and takes tree leaves, flowers,",
                    "branches, or fruits as memento.",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Tap for more details",
            }, {
                    "Uses high amount of fertilizers,",
                    "pesticides, and insecticides that",
                    "diminishes the soil quality.",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Tap for more details",
            }, {
                    "Promotes the desertification",
                    "of the forest area to provide",
                    "land for the farm.",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Tap for more details",
            }, {
                    "Responsible for fuel and oil",
                    "extraction that can be found",
                    "under forest lands. Also",
                    "creates chemicals that harms",
                    "ecosystems.",
                    "",
                    "",
                    "",
                    "",
                    "Tap for more details",
            }, {
                    "Destroys the natural resources",
                    "in pursuit of the minerals and",
                    "gold. Whether a large or small",
                    "company is involved, mining process",
                    "is reliant on heavy machinery",
                    "that destroys the forest land",
                    "upon entry.",
                    "",
                    "",
                    "Tap for more details",
            }, {
                    "Cuts down trees and burns the area",
                    "to be cultivated for farming. After",
                    "exhausting the resources, relocates",
                    "to another area to repeat the process.",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Tap for more details",
            }, {
                    "Cuts down trees without permit,",
                    "or even with permit but cuts more",
                    "trees than the allowed limit.",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Tap for more details",
            }, {
                    "Analytical person that makes way",
                    "to clear forests and construct",
                    "buildings and commercial",
                    "establishments in the forests' place.",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Tap for more details",
            }
    };

    public static Vector2[] getSpriteLocation(int index) {
        Vector2[] temp = {new Vector2(((index % 4) + ((index % 4) * 3)), (index / 4) * 4), new Vector2(((index % 4 + 3) + ((index % 4) * 3)), ((index / 4) * 4 + 3))};
        return temp;
    }
}
