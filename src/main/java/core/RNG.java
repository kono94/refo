package core;

import java.security.SecureRandom;
import java.util.Random;

public class RNG {
    private static SecureRandom rng;
    private static int seed = 123;
    static {
        rng = new SecureRandom();
        rng.setSeed(seed);
    }

    public static Random getRandom() {
        return rng;
    }

    public static void setSeed(int seed){
        RNG.seed = seed;
        rng.setSeed(seed);
    }
}
