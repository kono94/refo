package core;


public class Util {
    /**
     * Checks whether or not the given string input is a number or not.
     * Used for example to check the input field to trigger additional
     * episodes.
     *
     * @param strNum string input from user
     * @return true if numeric and parsable, false if not
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
