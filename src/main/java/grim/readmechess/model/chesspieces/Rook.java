package grim.readmechess.model.chesspieces;

import static grim.readmechess.utils.common.Constants.WHITE;

public class Rook extends Piece {
    public Rook(String color, String position) {
        super(color, color.equals(WHITE) ? "R" : "r", position);
    }
}
