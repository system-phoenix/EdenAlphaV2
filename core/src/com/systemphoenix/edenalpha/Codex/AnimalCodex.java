package com.systemphoenix.edenalpha.Codex;

public class AnimalCodex {

    public static final int LOWEST  = 0,
                            LOWER   = 1,
                            LOW     = 2,
                            MOD     = 3,
                            HIGH    = 4,
                            HIGHER  = 5,
                            HIGHEST = 6;

    public static final String name[] = {
            "Eagle", "Tamaraw", "Fruit Bat"
    };

    public static final float DMG[] = {
            25,
            75,
            100,
            150,
            175,
            200,
            300,
    };

    public static final float RANGE[] = {
            1f,         //lowest
            2f,         //lower
            2.5f,       //low
            3,          //mod
            3.5f,       //high
            4f,         //higher
            5f,         //highest
    };

    public static final int dmgStats[] = {
            MOD, LOWER, LOWEST
    };

    public static final int rangeStats[] = {
            LOW, HIGH, HIGHEST,
    };

    public static final int level[] = {
            2, 5, 8
    };

    public static final long effectLimit[] = {
            500, 5000, 7500
    };

    public static final String description[][] = {
            {
                    "",
                    "",
                    "",
                    ""
            }, {
                    "",
                    "",
                    "",
                    ""
            }, {
                    "",
                    "",
                    "",
                    "",
            }
    };
}
