package grim.readmechess.webapi.service.chesspieces;

import static grim.readmechess.utils.Constants.WHITE;

public class Queen extends Piece {
    public Queen(String color, String position) {
        super(color, "♛", color.equals(WHITE) ? "Q" : "q", position);
    }
}
