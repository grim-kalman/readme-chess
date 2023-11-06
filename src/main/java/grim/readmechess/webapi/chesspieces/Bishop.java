package grim.readmechess.webapi.chesspieces;

public class Bishop extends Piece {
    public Bishop(String color, String position) {
        super(color, "♝", color.equals("white") ? "B" : "b", position);
    }
}
