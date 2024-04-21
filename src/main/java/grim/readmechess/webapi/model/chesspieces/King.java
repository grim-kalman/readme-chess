package grim.readmechess.webapi.model.chesspieces;

import static grim.readmechess.utils.Constants.WHITE;

public class King extends Piece {
    public King(String color, String position) {
        super(color, color.equals(WHITE) ? "K" : "k", position);
    }
}
