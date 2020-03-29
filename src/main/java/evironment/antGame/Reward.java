package evironment.antGame;

public class Reward {
    public static final double FOOD_PICK_UP_SUCCESS = 0;
    public static final double FOOD_PICK_UP_FAIL_NO_FOOD = -1;
    public static final double FOOD_PICK_UP_FAIL_HAS_FOOD_ALREADY = -1;

    public static final double FOOD_DROP_DOWN_FAIL_NO_FOOD = -1;
    public static final double FOOD_DROP_DOWN_FAIL_NOT_START = -1;
    public static final double FOOD_DROP_DOWN_SUCCESS = 1;

    public static final double UNKNOWN_FIELD_EXPLORED = 0;

    public static final double RAN_INTO_WALL = -1;
    public static final double RAN_INTO_OBSTACLE = -1;
}
