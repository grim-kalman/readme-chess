package grim.readmechess.model.chesspieces;

import static grim.readmechess.utils.common.Constants.WHITE;

public class Pawn extends Piece {
    public Pawn(String color) {
        super(color, color.equals(WHITE) ? "P" : "p");
    }
}
