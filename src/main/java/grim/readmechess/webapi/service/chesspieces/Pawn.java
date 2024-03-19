package grim.readmechess.webapi.service.chesspieces;

import static grim.readmechess.utils.Constants.WHITE;

public class Pawn extends Piece {
    public Pawn(String color, String position) {
        super(color, "♟", color.equals(WHITE) ? "P" : "p", position);
    }
}
