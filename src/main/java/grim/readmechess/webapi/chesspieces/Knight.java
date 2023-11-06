package grim.readmechess.webapi.chesspieces;

public class Knight extends Piece {
    public Knight(String color, String position) {
        super(color, "♞", color.equals("white") ? "N" : "n", position);
    }
}
