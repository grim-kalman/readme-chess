package grim.readmechess.webapi.chesspieces;

public class Queen extends Piece {
    public Queen(String color, String position) {
        super(color, "♛", color.equals("white") ? "Q" : "q", position);
    }
}
