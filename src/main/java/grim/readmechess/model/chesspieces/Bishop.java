package grim.readmechess.model.chesspieces;

import static grim.readmechess.utils.common.Constants.WHITE;

public class Bishop extends Piece {
    public Bishop(String color, String position) {
        super(color, color.equals(WHITE) ? "B" : "b", position);
    }
}
