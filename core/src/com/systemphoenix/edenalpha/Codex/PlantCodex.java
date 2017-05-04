package com.systemphoenix.edenalpha.Codex;

import com.badlogic.gdx.math.Vector2;

public class PlantCodex {

    public static final float baseHP = 500, baseRange = 5, baseSeedRate = 1;
    public static final long baseAS = 1000;
    public static final Vector2 baseDmg = new Vector2(45, 50);

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

    public static final int level[] = {
            0,              //pine
            0,              //mango
            2,              //coconut
            3,              //mushroom
            4,              //acacia
            6,              //anahaw
            7,              //rafflesia
            9,              //mangrove
            10,             //santol
            11,             //duhat
            12,             //bamboo
            13,             //kapok
            14,             //narra
            15,             //balete
            16,             //pili
    };

    public static final String plantName[]= {
            "Pine",         //0
            "Mango",        //1
            "Coconut",      //2
            "Mushroom",     //3
            "Acacia",       //4
            "Anahaw",       //5
            "Rafflesia",    //6
            "Mangrove",     //7
            "Santol",       //8
            "Duhat",        //9
            "Bamboo",       //10
            "Kapok",        //11
            "Narra",        //12
            "Balete",       //13
            "Pili"          //14
    };

    public static final short typeBit[] = {
            1,              //pine
			1,              //mango
			1 | 2,          //coconut
			1 | 4,          //mushroom
            1,              //acacia
            1,              //anahaw
			1,              //rafflesia
			2,              //mangrove
            1,              //santol
			1,              //duhat
			1,              //kapok
            1,              //narra
			1 | 4,          //bamboo
			1,              //balete
			1               //pili
    };

    public static final float cost[] = {
            40,             //pine
			35,             //mango
			75,             //coconut
			25,             //mushroom
            150,            //acacia
            100,            //ananhaw
			150,            //rafflesia
			75,             //mangrove
            200,            //santol
			50,             //duhat
            350,            //bamboo
			350,            //kapok
			300,            //narra
			150,            //balete
			200             //pili
    };

    public static final int maxHP[] = {
            LOW,            //pine
			MOD,            //mango
			LOWEST,         //coconut
			VOID,           //mushroom
            LOWER,          //acacia
            ABS_LOWEST,     //anahaw
			LOWER,          //rafflesia
			ABS_LOWEST,     //mangrove
            LOWER,          //santol
			LOW,            //duhat
            VOID,           //bamboo
			ABS_HIGHEST,    //kapok
			HIGHEST,        //narra
			HIGH,           //balete
			LOWER           //pili
    };

    public static final int AS[] = {
            MOD,            //pine
			LOW,            //mango
			LOWER,          //coconut
			HIGHER,         //mushroom
            HIGH,           //acacia
            HIGH,           //anahaw
			LOWER,          //rafflesia
			MOD,            //mangrove
            MOD,            //santol
			HIGHEST,        //duhat
            ABS_LOWEST,     //bamboo
			ABS_HIGHEST,    //kapok
			HIGHER,         //narra
			VOID,           //balete
			HIGHER,         //pili
    };

    public static final int range[] = {
            LOW,            //pine
			LOWER,          //mango
			LOWER,          //coconut
			LOWEST,         //mushroom
            LOWER,          //acacia
            LOWER,          //anahaw
			ABS_LOWEST,     //rafflesia
			LOWEST,         //mangrove
            MOD,            //santol
			LOW,            //duhat
            MOD,            //bamboo
			ABS_HIGHEST,    //kapok
			MOD,            //narra
			ABS_LOWEST,     //balete
			HIGH            //pili
    };

    public static final float effectiveRange[] = {
            2.5f,           //pine
			1,              //mango
			0,              //coconut
			0,              //mushroom
            2,              //acacia
            0,              //anahaw
			2,              //rafflesia
			0,              //mangrove
            2.5f,           //santol
			1,              //duhat
            0,              //bamboo
			3,              //kapok
			2,              //narra
			1,              //balete
			1               //pili
    };

    public static final float growthTime[] = {
            35,             //pine
			25,             //mango
			30,             //coconut
			5,              //mushroom
            15,             //acacia
            25,             //anahaw
			10,             //rafflesia
			20,             //mangrove
            20,             //santol
			15,             //duhat
            45,             //bamboo
			60,             //kapok
			30,             //narra
			15,             //balete
			45              //pili
    };

    public static final int seedProduction[] = {
            LOWEST,         //pine
			HIGHER,         //mango
			HIGH,           //coconut
			ABS_LOWEST,     //mushroom
            LOW,            //acacia
            VOID,           //anahaw
			VOID,           //rafflesia
			VOID,           //mangrove
            LOWER,          //santol
			HIGHEST,        //duhat
            VOID,           //bamboo
			ABS_HIGHEST,    //kapok
			HIGH,           //narra
			VOID,           //balete
			MOD             //pili
    };

    public static final float projectileSize[] = {
            8,          //pine
			16,         //mango
			32,         //coconut
			8,          //mushroom
            16,         //acacia
            32,         //anahaw
			0,          //rafflesia
			0,          //mangrove
            16,         //santol
			8,          //duhat
            0,          //bamboo
			16,         //kapok
			8,          //narra
			0,          //balete
			16          //pili
    };

    public static final int[] DMG = {
            LOW,        //pine
            ABS_LOWEST,      //mango
            HIGHER,     //coconut
            ABS_LOWEST, //mushroom
            LOW,        //acacia
            MOD,        //anahaw
            LOWEST,     //rafflesia
            LOWEST,     //mangrove
            HIGH,       //santol
            ABS_LOWEST, //duhat
            ABS_HIGHEST,//bamboo
            ABS_LOWEST, //kapok
            LOWEST,     //narra
            VOID,       //balete
            HIGH        //pili
    };

    public static final String bulletFile[] = {
            "brown",    //pine
			"yellow",   //mango
			"brown",    //coconut
			"blue",     //mushroom
            "brown",    //acacia
            "green",    //anahaw
			"--",       //rafflesia
			"--",       //mangrove
            "brown",    //santol
			"purple",   //duhat
            "--",       //bamboo
			"blue",     //kapok
			"green",    //narra
			"--",       //balete
			"purple"    //pili
    };

    public static final float[] plantSelectorIndex = {
            368f, 464f, 560f, 656f, 752f, 848f, 976f
    };

    public static final String[][] description = {
            {//pine
                    "Mostly associated with cool",
                    "climates, commonly in places",
                    "like Baguio City and Mountain",
                    "Province. This gained Baguio",
                    "the nickname \"City of Pines\".",
                    "Grows upto 30 - 40 meters",
                    "with 140 cm diameter.",
                    "",
                    "",
                    "Tap for more details",
            }, {//mango
                    "The tree can reach 15",
                    "to 18 meters in height,",
                    "has round canopy, and is",
                    "larges preading. Blooms",
                    "flowers that may cause",
                    "respiratory and allergic",
                    "reactions.",
                    "",
                    "",
                    "Tap for more details",
            }, {//coconut
                    "Typically grows in tropical and",
                    "subtropical countries. Belongs",
                    "to the family of cherries and",
                    "peaches. Grows up to 30 meters",
                    "and the leaves have a length",
                    "of 4 - 6 meters. The fruit",
                    "weighs up to 1.44 kilograms.",
                    "",
                    "",
                    "Tap for more details",
            }, {//mushroom
                    "Also known as \"toad stools.\"",
                    "Fungal plants that can grow",
                    "almost anywhere. Does not require",
                    "sunlight to create energy. It is",
                    "composed of 90% water. Releases",
                    "spores that can cause allergic",
                    "reactions.",
                    "",
                    "",
                    "Tap for more details",
            }, {//acacia
                    "A large umbraculifera tree",
                    "that reaches height up to 20",
                    "to 25 meters. Grows rapidly",
                    "and has tolerance for poor",
                    "soils. Commonly used as shade",
                    "trees, but sometimes,",
                    "grows spontaneously.",
                    "",
                    "",
                    "Tap for more details",
            }, {//anahaw
                    "Known as the Philippine national",
                    "leaf, it has many uses and can",
                    "be transformed into many things.",
                    "Grows up to 15 - 20 meters with",
                    "25 cm diameter. Has a smooth",
                    "trunk and obscure rings",
                    "that are leaf marks.",
                    "",
                    "",
                    "Tap for more details",
            }, {//rafflesia
                    "Known as Rafflesia Arnoldii or",
                    "\"stinking corpse lily,\" it was",
                    "named after Sir Stanford Raffles and",
                    "Dr. James Arnold in 1988. It is a",
                    "jungle parasite that weighs up to 11",
                    "kilograms and blooms up to 107 cm, it",
                    "is the largest flower in the world.",
                    "",
                    "",
                    "Tap for more details",
            }, {//mangrove
                    "\"Rainforest of the Sea.\" Grows",
                    "in tropical countries. Protects",
                    "coastal communities from storm",
                    "surges and stabilizes coastline",
                    "through corrosion reduction. Vital",
                    "part of marine ecosystem that",
                    "involves seagrass and coral reefs.",
                    "",
                    "",
                    "Tap for more details",
            }, {//santol
                    "Fast-growing tree that is up to",
                    "15-45 meters tall. It has compound",
                    "leaves, composed of 3 leaflets.",
                    "The five-petalled flowers are borne",
                    "on the young branches.",
                    "The fruit is oblate with wrinkles",
                    "from the base.",
                    "",
                    "",
                    "Tap for more details",
            }, {//duhat
                    "A Duhat tree is about 8-14 meters",
                    "tall, with white branches and",
                    "reddish young shoots. The leaves",
                    "are elliptic, flower petals are ",
                    "arranged to form a cup. The fruit",
                    "is oval, 1-2 cm long, dark purple",
                    "to black",
                    "",
                    "",
                    "Tap for more details",
            }, {//bamboo
                    "Also called the \"Grass of hope,\"",
                    "it is a plant with high versatility,",
                    "thanks to its pliant characteristic",
                    "and tensile strength. It shields from",
                    "ultraviolet rays and decreases the",
                    "light intensity.",
                    "Found in varying climates.",
                    "",
                    "",
                    "Tap for more details",
            }, {//kapok
                    "Grows in tropical rainforests,",
                    "it can conquer new or deforested",
                    "lands. The tree can grow",
                    "up to 70 meters. It has an",
                    "umbrella-shaped crown made of",
                    "numerous branches. The branches",
                    "are covered with thorns.",
                    "",
                    "",
                    "Tap for more details",
            }, {//narra
                    "The Philippine National Tree,",
                    "grows up to 30 meters with a",
                    "diameter of 2 meters. It is a",
                    "sturdy tree that produces disc-",
                    "shaped pods that contains two,",
                    "three occaionally, seeds that are",
                    "dispersed through wind and air.",
                    "",
                    "",
                    "Tap for more details",
            }, {//balete
                    "Grows from a host tree",
                    "that it crushes to death,",
                    "it can reach up to 10-20 meters.",
                    "There have been instances where",
                    "it is related to ghost stories",
                    "and folklores, like the infamous",
                    "story about the Balete Drive.",
                    "",
                    "",
                    "Tap for more details",
            }, {//pili
                    "\"The next tree of life.\"",
                    "Its fruit has a thin skin. But the",
                    "core portion is very, hard to",
                    "crack, and it protects the kernel.",
                    "The trunk is used for furniture,",
                    "the shell of the fruit is used",
                    "for charcoal and handicrafts.",
                    "",
                    "",
                    "Tap for more details",
            },
    };

}
