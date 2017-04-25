package com.systemphoenix.edenalpha.Codex;

public class RegionCodex {

    public static final String[]
        codes = {
            "    CAR     ",
            "  Region I  ",
            " Region  II ",
            " Region III ",
            " Region IVA ",
            " Region IVB ",
            "  Region V  ",
            " Region  VI ",
            " Region VII ",
            " Region VIII",
            " Region  IX ",
            "  Region X  ",
            " Region  XI ",
            " Region XII ",
            " Region XIII",
            "    ARMM    ",
            "     NCR    "
        },
//            names = {
//            "  Cordillera Administrative Region  ",
//            "                Ilocos              ",
//            "            Cagayan Valley          ",
//            "            Central  Luzon          ",
//            "              CALABARZON            ",
//            "               MIMAROPA             ",
//            "                Bicol               ",
//            "           Western Visayas          ",
//            "           Central Visayas          ",
//            "           Eastern Visayas          ",
//            "         Zamboanga Peninsula        ",
//            "          Northern Mindanao         ",
//            "                Davao               ",
//            "             SOCCSKSARGEN           ",
//            "                Caraga              ",
//            "Autonomous Region in Muslim Mindanao",
//            "       National Capital Region      "
//        };
        names = {
            "Cordillera Administrative Region",
            "Ilocos",
            "Cagayan Valley",
            "Central Luzon",
            "CALABARZON",
            "MIMAROPA",
            "Bicol",
            "Western Visayas",
            "Central Visayas",
            "Eastern Visayas",
            "Zamboanga Peninsula",
            "Northern Mindanao",
            "Davao",
            "SOCCSKSARGEN",
            "Caraga",
            "Autonomous Region in Muslim Mindanao",
            "National Capital Region"
        };


    public static final float[]
        forestPercentage = {
            81, 37, 64, 44, 35, 64, 31, 30, 35, 52, 54, 52, 63, 61, 71, 51, 24
        },
        startingResource = {
            50, 100, 150, 200, 250, 300, 325, 350, 375, 400, 425, 450, 475, 500, 550, 600, 700
        },
        camX = {
            685f,   645f,  725f, 685f, 745f, 645f, 875f, 795f, 905f, 995f, 825f, 965f, 1065f, 985f, 1075f, 765f, 685f
        },
        camY = {
            1055f, 1095f, 1125f, 905f, 795f, 675f, 735f, 525f, 465f, 565f, 275f, 325f,  225f, 195f,  405f, 195f, 835f
        };

    public static final int mapIndeces[] = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
        };

    public static final int timeStart[] = {
            64, 64, 64, 64, 64, 64, 64, 64, 65, 64, 64, 64, 66, 65, 64, 64, 65
        };

    public static final float sunlight[] = {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.5f
        };

    public static final int waves[][][] = {
            {   //0,    5
                {0},
                {0, 0, 1},
                {0, 1},         //mid wave
                {1, 1, 0},
                {1},            //final wave
            }, {//1,    5
                {0},
                {0, 0, 1},
                {0, 1},         //mid wave
                {1, 1, 0},
                {1},            //final wave
            }, {//2,    6
                {0, 0, 1},
                {0, 1},
                {1},            //mid wave
                {2, 0},
                {2, 1, 0},
                {2},            //final wave
            }, {//3,    6
                {0, 0, 1},
                {0, 1},
                {1},            //mid wave
                {2, 0},
                {2, 1, 0},
                {2},            //final wave
            }, {//4,    7
                {0, 1, 3},
                {3, 1, 2, 0},
                {3, 2},
                {3, 0, 3},
                {2, 1},         //mid wave
                {1, 2, 3},
                {3, 0},
                {3},//final wave
            }, {//5,    7
                {0, 1, 3},
                {3, 1, 2, 0},
                {3, 2},
                {3, 0, 3},
                {2, 1},         //mid wave
                {1, 2, 3},
                {3, 0},
                {3},//final wave
            }, {//6,    8
                {1, 0},
                {2, 3},
                {3, 3, 3, 3, 4},
                {4, 1, 4, 1, 4},//mid wave
                {0, 4, 0, 4, 0},
                {1, 4, 1},
                {3, 3, 4},
                {4, 4, 2, 4, 2},//final wave
            }, {//7,    8
                {1, 0},
                {2, 3},
                {3, 3, 3, 3, 4},
                {4, 1, 4, 1, 4},//mid wave
                {0, 4, 0, 4, 0},
                {1, 4, 1},
                {3, 3, 4},
                {4, 4, 2, 4, 2},//final wave
            }, {//8,    9
                {0, 1, 2, 3, 4},
                {2, 3, 4, 3, 1},
                {3, 4, 2, 4, 3},
                {3, 4, 3, 4, 3},
                {1, 4},         //mid wave
                {4, 3, 2},
                {1, 2, 3, 4},
                {4, 2},
                {4}             //final wave
            }, {//9,    9
                {0, 1, 2, 3, 4},
                {2, 3, 4, 3, 1},
                {3, 4, 2, 4, 3},
                {3, 4, 3, 4, 3},
                {1, 4},         //mid wave
                {4, 3, 2},
                {1, 2, 3, 4},
                {4, 2},
                {4}             //final wave
            }, {//10,   10
                {3},
                {0, 3, 4},
                {0, 1, 2, 3, 4},
                {1, 2, 3},
                {4, 3, 4, 3, 4},//mid wave
                {0, 3, 0, 3, 5},
                {3, 2, 4, 2, 5},
                {5, 1, 4, 1, 3},
                {3, 0, 4, 0, 5},
                {5}             //final wave
            }, {//11,   10
                {3},
                {0, 3, 4},
                {0, 1, 2, 3, 4},
                {1, 2, 3},
                {4, 3, 4, 3, 4},//mid wave
                {0, 3, 0, 3, 5},
                {3, 2, 4, 2, 5},
                {5, 1, 4, 1, 3},
                {3, 0, 4, 0, 5},
                {5}             //final wave
            }, {//12,   11
                {0, 3, 0, 3, 4},
                {0, 1, 3, 2, 0},
                {3, 0, 4, 1, 2},
                {3, 4, 5, 4, 3},
                {0, 4, 5, 3, 0},
                {0, 0, 0, 0, 6},//mid wave
                {1, 0, 1, 0, 6},
                {2, 6, 2, 6, 2},
                {6, 4, 6, 4, 6},
                {6, 6, 6, 6, 5},
                {6}             //final wave
            }, {//13,   11
                {0, 3, 0, 3, 4},
                {0, 1, 3, 2, 0},
                {3, 0, 4, 1, 2},
                {3, 4, 5, 4, 3},
                {0, 4, 5, 3, 0},
                {0, 0, 0, 0, 6},//mid wave
                {1, 0, 1, 0, 6},
                {2, 6, 2, 6, 2},
                {6, 4, 6, 4, 6},
                {6, 6, 6, 6, 5},
                {6}             //final wave
            }, {//14,   12
                {0, 1, 2, 3},
                {1, 2, 3},
                {3, 4, 5, 6},
                {4, 5, 6},
                {6, 4, 5, 3},
                {6, 5},         //mid wave
                {6, 4},
                {6, 3},
                {6, 2},
                {6, 1},
                {6, 0},
                {7}             //final wave
            }, {
                {0, 1, 2, 3},
                {1, 2, 3},
                {3, 4, 5, 6},
                {4, 5, 6},
                {6, 4, 5, 3},
                {6, 5},         //mid wave
                {6, 4},
                {6, 3},
                {6, 2},
                {6, 1},
                {6, 0},
                {7}             //final wave
            }, {//16,   13
                {0, 1},
                {0, 2},
                {0, 3},
                {0, 4},
                {0, 5},
                {0, 6},
                {0, 7, 7, 7, 0},//mid wave
                {0, 2, 4, 6},
                {1, 3, 5, 7},
                {0, 1, 2, 3},
                {4, 5, 6, 7},
                {0},
                {7},             //final wave
            },
        };

    public static final String mapSound[] = {
            "01.ogg", "02.mp3", "03.ogg", "04.mp3", "05.ogg", "06.mp3", "07.ogg", "02.mp3", "04.mp3", "06.mp3", "01.ogg", "02.mp3", "03.ogg", "04.mp3", "05.ogg", "06.mp3", "07.ogg"
    };

}
