package grim.readmechess.webapi.model.chesspieces;

import static grim.readmechess.utils.Constants.WHITE;

public class Rook extends Piece {
    public Rook(String color, String position) {
        super(color, color.equals(WHITE) ? "R" : "r", position);
    }
}
