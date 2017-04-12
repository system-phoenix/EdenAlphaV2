package com.systemphoenix.edenalpha.Codex;

import com.badlogic.gdx.math.Vector2;

public class PlantCodex {

    public static final float baseHP = 500, baseRange = 5, baseSeedRate = 1;
    public static final long baseAS = 1000;
    public static final Vector2 baseDmg = new Vector2(90, 95);

    public static final int VOID        = 0,
                            ABS_LOWEST  = 1,
                            LOWEST      = 2,
                            LOWER       = 3,
                            LOW         = 4,
                            MOD         = 5,
                            HIGH        = 6,
                            HIGHER      = 7,
                            HIGHEST     = 8,
                            ABS_HIGHEST = 9;

    public static final String plantName[]= {
            "Pine",     "Mango",    "Coconut",  "Mushroom",     "Rafflesia",    "Mangrove",     "Acacia",   "Anahaw",   "Santol",   "Duhat",    "Kapok",    "Bamboo",   "Narra",    "Balete",   "Pili"
    };

    public static final short typeBit[] = {
            1,          1,          1 | 2,      1 | 4,          1,              2,              1,           1,         1,          1,          1,          1 | 4,      1,          1,          1
    };

    public static final float cost[] = {
            50,         25,         100,        15,             100,            75,             150,         120,       200,        20,        350,        350,        300,        150,        350
    };

    public static final int maxHP[] = {
            LOW,        MOD,     LOWEST,      VOID,          LOWER,          ABS_LOWEST,      LOWER,      ABS_LOWEST,  LOWER,      LOW,       ABS_HIGHEST,VOID,       HIGHER,     HIGH,      LOWER
    };

    public static final float hpStats[] = {
            10,         //void hp
            25,         //absolute lowest
            75,         //lowest
            100,        //lower
            150,        //low
            200,        //mod
            250,        //high
            300,        //higher
            325,        //highest
            baseHP,     //absolute highest
    };

    public static final int AS[] = {
            MOD,       LOW,       LOWER,       HIGHER,           MOD,           MOD,            HIGH,         HIGH,       MOD,       HIGHEST,        ABS_HIGHEST,        ABS_LOWEST,       HIGHER,        VOID,          HIGHER,
    };

    public static final long[] asStats = {
            0,          //void attack speed
            4000,       //absolute lowest
            3000,       //lowest
            2000,       //lower
            1500,       //low
            baseAS,     //mod
            750,        //high
            500,        //higher
            333,        //highest
            250,        //absolute highest
    };

    public static final int range[] = {
            LOW,       LOWER,      LOWER, LOWEST,      ABS_LOWEST,      LOWEST,      LOWER,           LOWER,     MOD,       LOW,       ABS_HIGHEST,  MOD,       MOD,        ABS_LOWEST, HIGH
    };

    public static final float rangeStats[] = {
            0f,         //void
            1f,         //absolute lowest
            1.5f,       //lowest
            2f,         //lower
            2.5f,       //low
            3,          //mod
            3.5f,       //high
            4f,         //higher
            4.5f,       //highest
            5,          //absolute highest
    };

    public static final float effectiveRange[] = {
            2.5f,       1,          0,          0,              2,              0,              2,           0,         2.5f,       1,          3,          0,          2,          1,          1
    };

    public static final float growthTime[] = {
            35,         15,         30,         5,              10,             20,             15,          25,        20,         15,         60,         45,         30,         15,         45
    };

    public static final int level[] = {
            0,          0,          1,          3,              4,              5,              6,           7,         8,          9,          10,         11,         12,         13,         14
    };

    public static final int seedProduction[] = {
            LOWEST,     HIGHER,        HIGH,      VOID,           VOID,           VOID,           LOW,        VOID,      LOWER,    HIGHEST, ABS_HIGHEST,       VOID,       HIGH,     VOID,       MOD
    };

    public static final float seedRateStats[] = {
            0,          //void
            0.01f,      //absolute lowest
            0.025f,     //lowest
            0.05f,      //lower
            0.075f,     //low
            0.1f,       //mod
            0.25f,      //high
            0.5f,       //higher
            0.75f,      //highest
            1f,         //absolute highest
    };

    public static final float projectileSize[] = {
            8,          16,         32,         8,              0,              0,              16,          32,        16,         8,          16,         0,          8,          0,          16
    };

    public static final int[] DMG = {
            LOW,        //pine
            LOWER,      //mango
            HIGHER,     //coconut
            LOWEST,     //mushroom
            LOWEST,     //rafflesia
            LOWEST,     //mangrove
            LOW,        //acacia
            MOD,        //anahaw
            HIGH,       //santol
            ABS_LOWEST, //duhat
            ABS_LOWEST, //kapok
            ABS_HIGHEST,//bamboo
            LOWEST,     //narra
            VOID,       //balete
            HIGH        //pili
    };

    public static final Vector2[] dmgStats = {
//            new Vector2(0, 0),      //void damage
//            new Vector2(0, 5),      //abs lowest
//            new Vector2(10, 15),    //lowest
//            new Vector2(20, 30),    //lower
//            new Vector2(30, 45),    //low
//            new Vector2(45, 55),    //mod
//            new Vector2(50, 65),    //high
//            new Vector2(70, 80),    //higher
//            new Vector2(80, 85),    //highest
//            new Vector2(90, 95),    //abs highest
            new Vector2(0, 0),      //void damage
            new Vector2(0, 5),      //abs lowest
            new Vector2(10, 15),    //lowest
            new Vector2(15, 20),    //lower
            new Vector2(20, 25),    //low
            new Vector2(25, 30),    //mod
            new Vector2(30, 35),    //high
            new Vector2(35, 40),    //higher
            new Vector2(40, 45),    //highest
            new Vector2(45, 50),    //abs highest
    };

    public static final String bulletFile[] = {
            "brown",    "yellow",   "brown",    "blue",         "--",           "--",           "brown",    "green",    "brown",   "purple",   "blue",     "--",        "green",    "--",       "purple"
    };

    public static final float[] plantSelectorIndex = {
            368f, 464f, 560f, 656f, 752f, 848f, 976f
    };

    public static final String[][] description = {
            {//pine
                    "Mostly associated with cool climates",
                    "but some species can grow on shores.",
                    "Grows up to 30 - 40 meters with 140",
                    "cm diameter."
            }, {//mango
                    "The tree can reach 15 to 18 meters",
                    "in height, has round canopy, and ",
                    "is large spreading. Blooms flowers",
                    "that may cause allergic reactions."
            }, {//coconut
                    "Grows up to 30 meters and the leaves",
                    "have a length of 4 - 6 meters. Belongs",
                    "to the family of cherries and peaches.",
                    "The fruit weighs up to 1.44 kilos."
            }, {//mushroom
                    "Fungal plants that can grow almost",
                    "anywhere. Many are edible but some",
                    "contains toxins that cause various",
                    "complications."
            }, {//Rafflesia
                    "Named after Sir Stanford Raffles and",
                    "Dr. James Arnold, it is the largest flower",
                    "that weighs up to 11 kilos and blooms up",
                    "to 107 centimeters. Smells like rotten meat."
            }, {//Mangrove
                    "A.k.a. \"rainforest of the sea.\" Protects",
                    "coastal communities from storm surges",
                    "and stabilizes coastline through",
                    "corrosion reduction."
            }, {//acacia
                    "Has rapid growth rate and tolerance",
                    "for poor soils, it can grow up to 30 meters.",
                    "",
                    ""
            }, {//anahaw
                    "The Philippine national leaf, it has",
                    "many uses and can be transformed",
                    "into many things.Its tree can grow up",
                    "to 24 meters, with a very thin trunk."
            }, {//santol
                    "Fast-growing tree that is up to",
                    "15-45 meters tall. It has compound",
                    "leaves, composed of 3 leaflets.",
                    "The fruit is oblate with wrinkles."
            }, {//duhat
                    "About 8-14 meters high with white",
                    "branches and reddish young shoots.",
                    "The fruit is oval, 1-2 cm long,",
                    "ranging from purple to black"
            }, {//kapok
                    "Grows in tropical rainforests, it can",
                    "conquer new or deforested areas.",
                    "The tree can grow up to 70 meters.",
                    "The branches are covered with thorns."
            }, {//bamboo
                    "\"Grass of hope,\" it is a plant with high",
                    "versatility, thanks to its pliant",
                    "characteristic and tensile strength.",
                    ""
            }, {//narra
                    "The Philippine national tree, grows up to",
                    "30 meters with a diameter of 2 meters.",
                    "Sturdy tree that produces disc-shaped",
                    "pods that contains the seeds."
            }, {//balete
                    "Grows from a host tree that it crushes",
                    "to death, it can reach up to",
                    "10-20 meters. There have been instances",
                    "where it is related to ghost stories."
            }, {//Pili
                    "\"The next tree of life.\" Its fruit has a thin",
                    "skin, but has a hard core part that",
                    "has the kernel.",
                    "The trunk is used for furniture."
            },
    };

}
