package grim.readmechess.webapi.service.chesspieces;

import static grim.readmechess.utils.Constants.WHITE;

public class Bishop extends Piece {
    public Bishop(String color, String position) {
        super(color, "♝", color.equals(WHITE) ? "B" : "b", position);
    }
}
