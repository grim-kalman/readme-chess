package grim.readmechess.model.chesspieces;

import lombok.Getter;

@Getter
public abstract class Piece {

    private final String color;
    private final String symbol;

    protected Piece(String color, String symbol) {
        this.color = color;
        this.symbol = symbol;
    }

}
