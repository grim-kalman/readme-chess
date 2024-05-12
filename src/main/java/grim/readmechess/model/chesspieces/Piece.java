package grim.readmechess.model.chesspieces;

public abstract class Piece {

    private final String color;
    private final String symbol;

    protected Piece(String color, String symbol) {
        this.color = color;
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }
}
