package grim.readmechess.webapi.model.chessboard;

import grim.readmechess.webapi.model.chesspieces.Piece;
import grim.readmechess.webapi.service.engineservice.EngineService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static grim.readmechess.utils.Utils.columnToIndex;
import static grim.readmechess.utils.Utils.rowToIndex;

@Component
public class BoardPrinter {

    private static final int BOARD_SIZE = 8;
    private static final String MARKDOWN_HEADER = "|     |  a  |  b  |  c  |  d  |  e  |  f  |  g  |  h  |";
    private static final String MARKDOWN_SEPARATOR = "|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|";

    private final Board board;
    private final EngineService engineService;

    public BoardPrinter(Board board, EngineService engineService) {
        this.board = board;
        this.engineService = engineService;
    }

    public String printMarkdown() {
        return convertToMarkdown(createBoardRepresentation());
    }

    public String printFEN() {
        return convertToFEN(createBoardRepresentation());
    }

    private String[][] createBoardRepresentation() {
        String[][] boardRepresentation = new String[BOARD_SIZE][BOARD_SIZE];
        for (Piece piece : board.getPieces()) {
            placePieceOnBoard(boardRepresentation, piece);
        }
        return boardRepresentation;
    }

    private void placePieceOnBoard(String[][] boardRepresentation, Piece piece) {
        int col = columnToIndex(piece.getPosition().charAt(0));
        int row = rowToIndex(piece.getPosition().charAt(1));
        boardRepresentation[row][col] = piece.getSymbol();
    }

    private String convertToMarkdown(String[][] boardRepresentation) {
        String table = buildMarkdownTable(boardRepresentation);
        return String.format("%s%n%s%n%s", MARKDOWN_HEADER, MARKDOWN_SEPARATOR, table);
    }

    private String buildMarkdownTable(String[][] boardRepresentation) {
        StringBuilder tableBuilder = new StringBuilder();
        List<String> validMoves = engineService.getValidMoves();
        for (int i = 0; i < BOARD_SIZE; i++) {
            tableBuilder.append(formatMarkdownRow(boardRepresentation[i], BOARD_SIZE - i, validMoves));
            if (i < BOARD_SIZE - 1) {
                tableBuilder.append("\n");
            }
        }
        return tableBuilder.toString();
    }

    private String formatMarkdownRow(String[] row, int rowNumber, List<String> validMoves) {
        String rowContent = rowToMarkdown(row, rowNumber, validMoves);
        return String.format("|  %d  |  %s  |", rowNumber, rowContent);
    }

    private String rowToMarkdown(String[] row, int rowNumber, List<String> validMoves) {
        StringBuilder rowContentBuilder = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            String squareMarkdown = squareToMarkdown(row[i], (char) ('a' + i) + Integer.toString(rowNumber), validMoves);
            rowContentBuilder.append(squareMarkdown);
            if (i < row.length - 1) {
                rowContentBuilder.append("  |  ");
            }
        }
        return rowContentBuilder.toString();
    }

    private String squareToMarkdown(String square, String position, List<String> validMoves) {
        String selectedSquare = board.getSelectedSquare();
        String squareSymbol = square != null ? square : "_";

        if (square != null) {
            if (Character.isLowerCase(square.charAt(0))) {
                squareSymbol = "_" + square + "_";
            } else {
                squareSymbol = "**" + square + "**";
            }
        }

        if (selectedSquare != null && isValidMove(selectedSquare + position, validMoves)) {
            return formatMarkdownLink(squareSymbol, "play", selectedSquare + position, "move");
        } else if (isStartOfValidMove(position, validMoves)) {
            return formatMarkdownLink(squareSymbol, "select", position, "square");
        } else {
            return square == null ? " " : squareSymbol;
        }
    }

    private boolean isValidMove(String move, List<String> validMoves) {
        return validMoves.stream().anyMatch(validMove -> validMove.equals(move));
    }

    private boolean isStartOfValidMove(String position, List<String> validMoves) {
        return validMoves.stream().anyMatch(validMove -> validMove.startsWith(position));
    }

    private String formatMarkdownLink(String squareSymbol, String action, String parameter, String queryParamName) {
        return String.format("[%s](http://localhost:8080/api/chess/%s?%s=%s)", squareSymbol, action, queryParamName, parameter);
    }

    private String convertToFEN(String[][] boardRepresentation) {
        String boardFen = Arrays.stream(boardRepresentation)
                .map(this::convertRowToFen)
                .collect(Collectors.joining("/"));
        return String.format("%s %s %s %s %d %d",
                boardFen,
                board.getBoardState().getActiveColor(),
                board.getBoardState().getCastlingAvailability(),
                board.getBoardState().getEnPassantTarget(),
                board.getBoardState().getHalfMoveClock(),
                board.getBoardState().getFullMoveNumber());
    }

    private String convertRowToFen(String[] row) {
        return Arrays.stream(row)
                .map(square -> square == null ? "1" : square)
                .collect(Collectors.joining())
                .replace("11111111", "8")
                .replace("1111111", "7")
                .replace("111111", "6")
                .replace("11111", "5")
                .replace("1111", "4")
                .replace("111", "3")
                .replace("11", "2");
    }
}
