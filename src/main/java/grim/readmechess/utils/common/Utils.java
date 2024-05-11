package grim.readmechess.utils.common;

public class Utils {
    public static int columnToIndex(char column) {
        return column - 'a';
    }

    public static int rowToIndex(char row) {
        return '8' - row;
    }
}
