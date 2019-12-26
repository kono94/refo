package core;

import java.security.SecureRandom;
import java.util.Random;

/**
 * To ensure deterministic behaviour of repeating program executions,
 * this class is used for all random number generation methods.
 * Do not use Math.random()!
 * It is not necessary to set a seed explicit, because a default one
 * "123" is defined. Nonetheless a set-method is exposed which should
 * ONLY be called in the very beginning of the program. (Do not reseed while
 * execution)
 */
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
