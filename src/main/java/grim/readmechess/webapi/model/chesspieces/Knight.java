package grim.readmechess.webapi.model.chesspieces;

import static grim.readmechess.utils.Constants.WHITE;

public class Knight extends Piece {
    public Knight(String color, String position) {
        super(color, "♞", color.equals(WHITE) ? "N" : "n", position);
    }
}
