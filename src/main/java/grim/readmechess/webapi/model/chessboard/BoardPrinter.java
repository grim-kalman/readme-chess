package grim.readmechess.webapi.model.chessboard;

import grim.readmechess.webapi.model.chesspieces.Piece;

import java.util.Arrays;
import java.util.stream.Collectors;

import static grim.readmechess.utils.Utils.convertColumnToIndex;
import static grim.readmechess.utils.Utils.convertRowToIndex;
import static grim.readmechess.webapi.model.chessboard.Board.SQUARE_SIZE;

public class BoardPrinter {
    private static final String LIGHT_COLOR = "#D8AF87";
    private static final String DARK_COLOR = "#B58863";
    private static final String SQUARE_TEMPLATE = "<rect x=\"%d\" y=\"%d\" width=\"40\" height=\"40\" fill=\"%s\"/>";
    private static final String EDGE_LABELS_TEMPLATE = "<text x=\"%d\" y=\"%d\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">%s</text>";

    private final Board board;

    public BoardPrinter(Board board) {
        this.board = board;
    }

    public String printFEN() {
        String[][] boardRepresentation = new String[8][8];

        board.getPieces().forEach(piece -> updateBoardRepresentation(boardRepresentation, piece));

        String boardFen = Arrays.stream(boardRepresentation)
                .map(this::convertRowToFenNotation)
                .collect(Collectors.joining("/"));

        return String.format("%s %s %s %s %d %d",
                boardFen,
                board.getBoardState().getActiveColor(),
                board.getBoardState().getCastlingAvailability(),
                board.getBoardState().getEnPassantTarget(),
                board.getBoardState().getHalfMoveClock(),
                board.getBoardState().getFullMoveNumber());
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
        builder.append(count > 0 ? count : "");
    }

    private void appendPieceSymbol(StringBuilder builder, String symbol, int emptySquares) {
        appendEmptySquares(builder, emptySquares);
        builder.append(symbol);
    }

    public String printSVG() {
        return String.format("<svg viewBox=\"0 0 400 400\" xmlns=\"http://www.w3.org/2000/svg\">%s%s%s</svg>",
                printSquares(),
                printEdgeLabels(),
                printPieces());
    }

    private String printPieces() {
        return board.getPieces().stream()
                .map(Piece::toSvgString)
                .collect(Collectors.joining());
    }

    private String printEdgeLabels() {
        StringBuilder stringBuilder = new StringBuilder(3340);
        for (char col = 'a'; col <= 'h'; col++) {
            int x = 60 + SQUARE_SIZE * (col - 'a');
            stringBuilder.append(String.format(EDGE_LABELS_TEMPLATE, x, 20, col));
            stringBuilder.append(String.format(EDGE_LABELS_TEMPLATE, x, 380, col));
        }
        for (int row = 1; row <= 8; row++) {
            int y = 380 - SQUARE_SIZE * (row);
            stringBuilder.append(String.format(EDGE_LABELS_TEMPLATE, 20, y, row));
            stringBuilder.append(String.format(EDGE_LABELS_TEMPLATE, 380, y, row));
        }
        return stringBuilder.toString();
    }

    private String printSquares() {
        StringBuilder stringBuilder = new StringBuilder(3872);
        for (int row = 1; row <= 8; row++) {
            int y = SQUARE_SIZE * row;
            for (int col = 1; col <= 8; col++) {
                int x = SQUARE_SIZE * col;
                String color = (row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR;
                stringBuilder.append(String.format(SQUARE_TEMPLATE, x, y, color));
            }
        }
        return stringBuilder.toString();
    }
}
