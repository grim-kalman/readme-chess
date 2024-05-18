package grim.readmechess.utils.common;

public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int columnToIndex(char column) {
        return column - 'a';
    }

    public static int rowToIndex(char row) {
        return '8' - row;
    }
}
