package grim.readmechess.utils;

public class Utils {
    // Private constructor to prevent instantiation of this class as it is a utility class
    private Utils() {}
    public static int convertColumnToIndex(char column) {
        return column - 'a';
    }

    public static int convertRowToIndex(char row) {
        return '8' - row;
    }
}
