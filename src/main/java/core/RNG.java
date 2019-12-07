package core;

import java.util.Random;

public class RNG {
    private static final Random rng;
    private static final int SEED = 123;
    static {
        rng = new Random(SEED);
    }

    public static Random getRandom() {
        return rng;
    }

    public static void reseed(){
        rng.setSeed(SEED);
    }
}
