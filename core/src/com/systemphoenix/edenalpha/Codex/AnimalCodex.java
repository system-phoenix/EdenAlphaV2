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
            "Monkey-Eating Eagle", "Tamaraw", "Fruit Bat"
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
            1, 5, 8
    };

    public static final long effectLimit[] = {
            500, 5000, 7500
    };

    public static final String description[][] = {
            {
                    "A giant forest predator that can only be",
                    "found in the Philippines. Grows three feet",
                    "in height and has a wingspan of seven",
                    "feet. It is solitary and territorial. It",
                    "adapts in places with high forest density.",
            }, {
                    "Found only in Philippines, specifically in",
                    "Mindoro. It grows up to a little more than",
                    "2 meters in length, and weighs 200-300",
                    "kilos. Signals ferocity through the lowering",
                    "of horns, becomes violent when cornered.",
            }, {
                    "Endemic in the Philippines, it grows up to",
                    "14 centimeters in length, with a wingspan",
                    "of 55 centimeters. It is, with the continued",
                    "destruction of its habitat, endangered. It is",
                    "not seen for over 10 years in some places.",
            }
    };
}
