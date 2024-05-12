package grim.readmechess.model.chesspieces;

import static grim.readmechess.utils.common.Constants.WHITE;

public class Rook extends Piece {
    public Rook(String color) {
        super(color, color.equals(WHITE) ? "R" : "r");
    }
}
