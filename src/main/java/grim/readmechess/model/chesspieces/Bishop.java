package grim.readmechess.model.chesspieces;

import static grim.readmechess.utils.common.Constants.WHITE;

public class Bishop extends Piece {
    public Bishop(String color) {
        super(color, color.equals(WHITE) ? "B" : "b");
    }
}
