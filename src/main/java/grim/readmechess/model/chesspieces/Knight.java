package grim.readmechess.model.chesspieces;

import static grim.readmechess.utils.common.Constants.WHITE;

public class Knight extends Piece {
    public Knight(String color, String position) {
        super(color, color.equals(WHITE) ? "N" : "n", position);
    }
}
