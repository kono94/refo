package core;

import java.util.Random;

public class RNG {
    private static Random rng;
    private static int seed = 123;
    static {
        rng = new Random(seed);
    }

    public static Random getRandom() {
        return rng;
    }

    public static void setSeed(int seed){
        RNG.seed = seed;
        rng.setSeed(seed);
    }
}
