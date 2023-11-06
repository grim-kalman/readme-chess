package grim.readmechess.webapi.chesspieces;

public class Rook extends Piece {
    public Rook(String color, String position) {
        super(color, "♜", color.equals("white") ? "R" : "r", position);
    }
}
