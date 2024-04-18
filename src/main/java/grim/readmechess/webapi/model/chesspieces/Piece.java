package grim.readmechess.webapi.model.chesspieces;

import grim.readmechess.utils.Utils;

import static grim.readmechess.utils.Constants.WHITE;
import static grim.readmechess.webapi.model.chessboard.Board.SQUARE_SIZE;

public abstract class Piece {
    private static final int OFFSET_X = 60;
    private static final int OFFSET_Y = 66;
    private final String color;
    private final String symbol;
    private final String fenSymbol;
    private String position;

    protected Piece(String color, String symbol, String fenSymbol, String position) {
        this.color = color;
        this.symbol = symbol;
        this.fenSymbol = fenSymbol;
        this.position = position;
    }

    public String getFenSymbol() {
        return fenSymbol;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String toSvgString() {
        int col = Utils.convertColumnToIndex(position.charAt(0));
        int row = Utils.convertRowToIndex(position.charAt(1));
        String x = String.valueOf(OFFSET_X + col * SQUARE_SIZE);
        String y = String.valueOf(OFFSET_Y + row * SQUARE_SIZE);
        String pieceColor = this.color.equals(WHITE) ? "white" : "black";
        return String.format(
                "<text x=\"%s\" y=\"%s\" dominant-baseline=\"middle\" fill=\"%s\" font-size=\"36\" text-anchor=\"middle\">%s</text>",
                x, y, pieceColor, symbol);
    }
}