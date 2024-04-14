package grim.readmechess.utils;

public class Utils {
    private Utils() {}

    public static int convertColumnToIndex(char column) {
        return column - 'a';
    }
    public static int convertRowToIndex(char row) {
        return '8' - row;
    }
}
