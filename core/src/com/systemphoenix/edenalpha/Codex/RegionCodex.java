package com.systemphoenix.edenalpha.Codex;

import com.badlogic.gdx.math.Vector2;

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
        }, names = {
            "  Cordillera Administrative Region  ",
            "                Ilocos              ",
            "            Cagayan Valley          ",
            "            Central  Luzon          ",
            "              CALABARZON            ",
            "               MIMAROPA             ",
            "                Bicol               ",
            "           Western Visayas          ",
            "           Central Visayas          ",
            "           Eastern Visayas          ",
            "         Zamboanga Peninsula        ",
            "          Northern Mindanao         ",
            "                Davao               ",
            "             SOCCSKSARGEN           ",
            "                Caraga              ",
            "Autonomous Region in Muslim Mindanao",
            "       National Capital Region      "
        };


    public static final float[]
        forestPercentage = {
            81, 37, 64, 44, 35, 64, 31, 30, 35, 52, 54, 52, 63, 61, 71, 51, 24
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
            63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63
        };

}
