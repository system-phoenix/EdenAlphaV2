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
            75, 75, 150, 150, 200, 200, 250, 250, 300, 300, 350, 350, 400, 400, 450, 450, 500
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
                {0},
                {0},             //mid wave
                {0},
                {0, 0, 0, 0, 2},//final wave
            }, {//1,    5
                {1},
                {1},
                {1},            //mid wave
                {1},
                {1, 1, 1, 1, 3},//final wave
            }, {//2,    6
                {0},
                {0, 0, 0, 0, 2},
                {0, 0, 0, 2},         //mid wave
                {0, 0, 2},
                {0, 2},
                {2},            //final wave
            }, {//3,    6
                {1},
                {1, 1, 1, 1, 3},
                {1, 1, 1, 3},         //mid wave
                {1, 1, 3},
                {1, 3},
                {3},            //final wave
            }, {//4,    7
                {0, 0, 2},
                {2, 0, 2, 0},
                {0, 2},
                {0, 0, 2},
                {2},         //mid wave
                {0, 2, 2},
                {2, 0},
                {2},//final wave
            }, {//5,    7
                {1, 1, 3},
                {3, 1, 3, 1},
                {1, 3},
                {1, 1, 3},
                {3},         //mid wave
                {1, 3, 3},
                {3, 1},
                {3},//final wave
            }, {//6,    8
                {2, 0},
                {2},
                {2, 2, 2, 2, 4},
                {4, 0, 4, 0, 4},//mid wave
                {0, 4, 0, 4, 0},
                {0, 4, 0},
                {2, 2, 4},
                {4, 4, 2, 4, 2},//final wave
            }, {//7,    8
                {3, 1},
                {3},
                {3, 3, 3, 3, 5},
                {5, 1, 5, 1, 5},//mid wave
                {1, 5, 1, 5, 1},
                {1, 5, 1},
                {3, 3, 5},
                {5, 5, 3, 5, 3},//final wave
            }, {//8,    9
                {0, 0, 2, 2, 4},
                {2, 2, 4, 2, 0},
                {2, 4, 2, 4, 2},
                {4, 2, 4, 2, 4},
                {0, 4},         //mid wave
                {4, 2, 2},
                {0, 2, 2, 4},
                {4, 2},
                {4}             //final wave
            }, {//9,    9
                {1, 1, 3, 3, 5},
                {3, 3, 5, 3, 1},
                {3, 5, 3, 5, 3},
                {5, 3, 5, 3, 5},
                {1, 5},         //mid wave
                {5, 3, 3},
                {1, 3, 3, 5},
                {5, 3},
                {5}              //final wave
            }, {//10,   10
                {2},
                {0, 2, 4},
                {0, 0, 2, 2, 4},
                {0, 2, 2},
                {4, 2, 4, 2, 4},//mid wave
                {0, 2, 0, 2, 4},
                {2, 2, 4, 2, 4},
                {4, 0, 4, 0, 2},
                {2, 0, 4, 0, 4},
                {4}             //final wave
            }, {//11,   10
                {3},
                {1, 3, 5},
                {1, 1, 3, 3, 5},
                {1, 3, 3},
                {5, 3, 5, 3, 5},//mid wave
                {1, 3, 1, 3, 5},
                {3, 3, 5, 3, 5},
                {5, 1, 5, 1, 3},
                {3, 1, 5, 1, 5},
                {5}             //final wave
            }, {//12,   11
                {0, 2, 0, 2, 4},
                {0, 0, 2, 2, 0},
                {2, 0, 4, 0, 2},
                {2, 4, 4, 4, 2},
                {0, 4, 4, 2, 0},
                {0, 0, 0, 0, 6},//mid wave
                {0, 0, 6, 0, 6},
                {2, 6, 2, 6, 2},
                {6, 4, 6, 4, 6},
                {4, 6, 4, 6, 4},
                {6, 0, 2, 0, 6},//final wave
            }, {//13,   11
                {1, 3, 1, 3, 5},
                {1, 1, 3, 3, 1},
                {3, 1, 5, 1, 3},
                {3, 5, 5, 5, 3},
                {1, 5, 5, 3, 1},
                {1, 1, 1, 1, 7},//mid wave
                {1, 1, 7, 1, 7},
                {3, 7, 3, 7, 3},
                {7, 5, 7, 5, 7},
                {5, 7, 5, 7, 5},
                {7, 1, 3, 1, 7},//final wave
            }, {//14,   12
                {0, 0, 2, 2},
                {0, 2, 2},
                {2, 4, 4, 6},
                {4, 4, 6},
                {6, 4, 4, 2},
                {2, 6, 2, 4, 2},         //mid wave
                {2, 6, 4, 2},
                {6, 2},
                {6, 2},
                {6, 0},
                {6, 0},
                {6, 0 ,0, 6}             //final wave
            }, {
                {1, 1, 3, 3},
                {1, 3, 3},
                {3, 5, 5, 7},
                {5, 5, 7},
                {7, 5, 5, 3},
                {3, 7, 3, 5, 3},         //mid wave
                {3, 7, 5, 3},
                {7, 3},
                {7, 3},
                {7, 1},
                {7, 1},
                {7, 1 ,1, 7}             //final wave
            }, {//16,   13
                {0, 1},
                {2, 3},
                {4, 5},
                {0, 6, 1, 7, 0},
                {0, 0, 0, 0, 6},
                {1, 1, 1, 1, 7},
                {0, 6, 1, 7},//mid wave
                {0, 2, 4, 6},
                {1, 3, 5, 7},
                {0, 1, 2, 3},
                {4, 5, 6, 7},
                {0, 1},
                {6, 7},             //final wave
            },
        };

    public static final String mapSound[] = {
            "01.ogg", "02.mp3", "03.ogg", "04.mp3", "05.ogg", "06.mp3", "07.ogg", "02.mp3", "04.mp3", "06.mp3", "01.ogg", "02.mp3", "03.ogg", "04.mp3", "05.ogg", "06.mp3", "07.ogg"
    };

}
