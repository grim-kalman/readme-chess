package grim.readmechess.webapi.chesspieces;

public class King extends Piece {
    public King(String color, String position) {
        super(color, "♚", color.equals("white") ? "K" : "k", position);
    }
}
