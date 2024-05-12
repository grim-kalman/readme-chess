package grim.readmechess.model.chesspieces;

import static grim.readmechess.utils.common.Constants.WHITE;

public class King extends Piece {
    public King(String color) {
        super(color, color.equals(WHITE) ? "K" : "k");
    }
}
