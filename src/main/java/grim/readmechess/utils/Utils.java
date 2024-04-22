package grim.readmechess.utils;

public class Utils {
    private Utils() {}

    public static int columnToIndex(char column) {
        return column - 'a';
    }
    public static int rowToIndex(char row) {
        return '8' - row;
    }
}
