package core;
import java.util.Random;

/**
 * ! SecureRandom not working properly on windows/different JDKs,
 * using Random again !
 *
 * To ensure deterministic behaviour of repeating program executions,
 * this class is used for all random number generation methods.
 * Do not use Math.random()!
 * It is not necessary to set a seed explicit, because a default one
 * "123" is defined. Nonetheless a set-method is exposed which should
 * ONLY be called in the very beginning of the program. (Do not reseed while
 * execution)
 */
public class RNG {
    private static Random rng;
    private static Random rngEnv;
    private static int seed = 123;
    private static int envSeed = 13;
    static {
        rng = new Random();
        rngEnv = new Random();
        setSeed(seed, true);
    }

    public static Random getRandom() {
        return rng;
    }
    public static Random getRandomEnv() {
        return rngEnv;
    }

    public static void setSeed(int seed, boolean setEnvRandom) {
        RNG.seed = seed;
        rng.setSeed(seed);
        if(setEnvRandom) {
            rngEnv.setSeed(13);
        }
    }

    public static void setSeed(int seed) {
        setSeed(seed, true);
    }

}
