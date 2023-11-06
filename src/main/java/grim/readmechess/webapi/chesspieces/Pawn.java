package grim.readmechess.webapi.chesspieces;

public class Pawn extends Piece {
    public Pawn(String color, String position) {
        super(color, "♟", color.equals("white") ? "P" : "p", position);
    }
}
