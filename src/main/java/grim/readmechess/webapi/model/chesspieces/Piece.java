package grim.readmechess.webapi.model.chesspieces;

public abstract class Piece {

    private final String color;
    private final String symbol;
    private String position;

    protected Piece(String color, String symbol, String position) {
        this.color = color;
        this.symbol = symbol;
        this.position = position;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
