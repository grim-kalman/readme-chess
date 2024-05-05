package grim.readmechess.webapi.model.chessboard;

import grim.readmechess.webapi.model.chesspieces.Piece;
import grim.readmechess.webapi.service.engineservice.EngineService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    // TODO: make sure the winning side is displayed in the markdown
    public String printMarkdown() {
        double evaluation = engineService.getEngineResponse().evaluation();
        String formattedEvaluation = evaluation > 0 ? "+" + evaluation : String.valueOf(evaluation);
        String formattedGameStatus = engineService.getEngineResponse().isGameOver() ? "Game Over" : "In Progress";
        return String.format("# Readme Chess%n%n**Evaluation:** %s%n%n**Game Status:** %s%n%s%n%s%n%s",
                formattedEvaluation,
                formattedGameStatus,
                MARKDOWN_HEADER,
                MARKDOWN_SEPARATOR,
                buildMarkdownTable(createBoardRepresentation()));
    }

    public String printFEN() {
        return convertToFEN(createBoardRepresentation());
    }

    private String[][] createBoardRepresentation() {
        String[][] boardRepresentation = new String[BOARD_SIZE][BOARD_SIZE];
        for (Piece piece : board.getPieces()) {
            int col = columnToIndex(piece.getPosition().charAt(0));
            int row = rowToIndex(piece.getPosition().charAt(1));
            boardRepresentation[row][col] = piece.getSymbol();
        }
        return boardRepresentation;
    }

    private String buildMarkdownTable(String[][] boardRepresentation) {
        List<String> validMoves = engineService.getValidMoves();
        return IntStream.range(0, 8)
                .mapToObj(i -> formatMarkdownRow(boardRepresentation[i], 8 - i, validMoves))
                .collect(Collectors.joining("\n"));
    }

    private String formatMarkdownRow(String[] row, int rowNumber, List<String> validMoves) {
        return String.format("|  **%d**  |  %s  |", rowNumber, rowToMarkdown(row, rowNumber, validMoves));
    }

    private String rowToMarkdown(String[] row, int rowNumber, List<String> validMoves) {
        return IntStream.range(0, row.length)
                .mapToObj(i -> squareToMarkdown(row[i], (char) ('a' + i) + Integer.toString(rowNumber), validMoves))
                .collect(Collectors.joining("  |  "));
    }

    private String squareToMarkdown(String square, String position, List<String> validMoves) {
        String selectedSquare = board.getSelectedSquare();
        String squareSymbol = formatSquareSymbol(square);

        if (selectedSquare != null && isValidMove(selectedSquare + position, validMoves)) {
            return String.format("[%s](http://localhost:8080/api/chess/play?move=%s)", squareSymbol, selectedSquare + position);
        }
        if (isStartOfValidMove(position, validMoves)) {
            return String.format("[%s](http://localhost:8080/api/chess/select?square=%s)", squareSymbol, position);
        }
        if (square == null) {
            return " ";
        }
        return Character.isUpperCase(square.charAt(0)) ? String.format("[%s](https://github.com/grim-kalman)", squareSymbol) : squareSymbol;
    }

    private String formatSquareSymbol(String square) {
        if (square == null) {
            return "_";
        }
        return Character.isLowerCase(square.charAt(0)) ? "_" + square + "_" : "**" + square + "**";
    }

    private boolean isValidMove(String move, List<String> validMoves) {
        return validMoves.contains(move);
    }

    private boolean isStartOfValidMove(String position, List<String> validMoves) {
        return validMoves.stream().anyMatch(validMove -> validMove.startsWith(position));
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
