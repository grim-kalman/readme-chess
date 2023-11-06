package grim.readmechess.webapi.chesspieces;

import grim.readmechess.utils.Utils;

public abstract class Piece {
    private final String color;
    private final String symbol;
    private final String fenSymbol;
    private String position;

    public Piece(String color, String symbol, String fenSymbol, String position) {
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
        String x = String.valueOf(60 + col * 40);
        String y = String.valueOf(60 + row * 40);
        return String.format(
                "<text x=\"%s\" y=\"%s\" dominant-baseline=\"middle\" fill=\"%s\" font-size=\"40\" text-anchor=\"middle\">%s</text>",
                x, y, color, symbol);
    }
}
