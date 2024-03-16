package grim.readmechess.webapi.chessboard;

import grim.readmechess.webapi.chesspieces.Piece;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static grim.readmechess.utils.Utils.convertColumnToIndex;
import static grim.readmechess.utils.Utils.convertRowToIndex;

public class BoardPrinter {
    private final Board board;

    public BoardPrinter(Board board) {
        this.board = board;
    }

    public String printFEN() {
        List<Piece> pieces = board.getPieces();
        String activeColor = board.getActiveColor();
        String castlingAvailability = board.getCastlingAvailability();
        String enPassantTarget = board.getEnPassantTarget();
        int halfMoveClock = board.getHalfMoveClock();
        int fullMoveNumber = board.getFullMoveNumber();
        String[][] boardRepresentation = new String[8][8];

        pieces.forEach(piece -> updateBoardRepresentation(boardRepresentation, piece));

        String boardFen = Arrays.stream(boardRepresentation)
                .map(this::convertRowToFenNotation)
                .collect(Collectors.joining("/"));

        return String.format("%s %s %s %s %d %d",
                boardFen, activeColor, castlingAvailability, enPassantTarget, halfMoveClock, fullMoveNumber);
    }

    private void updateBoardRepresentation(String[][] boardRepresentation, Piece piece) {
        String position = piece.getPosition();
        int col = convertColumnToIndex(position.charAt(0));
        int row = convertRowToIndex(position.charAt(1));
        boardRepresentation[row][col] = piece.getFenSymbol();
    }

    private String convertRowToFenNotation(String[] row) {
        StringBuilder rowFen = new StringBuilder();
        int emptySquares = 0;

        for (String square : row) {
            if (square == null) {
                emptySquares++;
            } else {
                appendPieceSymbol(rowFen, square, emptySquares);
                emptySquares = 0;
            }
        }

        appendEmptySquares(rowFen, emptySquares);
        return rowFen.toString();
    }

    private void appendEmptySquares(StringBuilder builder, int count) {
        if (count > 0) {
            builder.append(count);
        }
    }

    private void appendPieceSymbol(StringBuilder builder, String symbol, int emptySquares) {
        appendEmptySquares(builder, emptySquares);
        builder.append(symbol);
    }
}
