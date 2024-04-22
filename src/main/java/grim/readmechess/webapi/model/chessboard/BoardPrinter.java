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
        String[][] boardRepresentation = new String[8][8];
        for (Piece piece : board.getPieces()) {
            updateBoardRepresentation(boardRepresentation, piece);
        }
        return boardRepresentation;
    }

    private void updateBoardRepresentation(String[][] boardRepresentation, Piece piece) {
        int col = columnToIndex(piece.getPosition().charAt(0));
        int row = rowToIndex(piece.getPosition().charAt(1));
        boardRepresentation[row][col] = piece.getSymbol();
    }

    private String convertToMarkdown(String[][] boardRepresentation) {
        String header =    "|     |  a  |  b  |  c  |  d  |  e  |  f  |  g  |  h  |";
        String separator = "|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|";
        String table = IntStream.range(0, boardRepresentation.length)
                .mapToObj(i -> rowToMarkdown(boardRepresentation[i], boardRepresentation.length - i))
                .collect(Collectors.joining("\n"));

        return String.format("%s%n%s%n%s", header, separator, table);
    }

    private String rowToMarkdown(String[] row, int rowNumber) {
        List<String> validMoves = engineService.getValidMoves();
        String rowContent = IntStream.range(0, row.length)
                .mapToObj(i -> squareToMarkdown(row[i], (char) ('a' + i) + Integer.toString(rowNumber), validMoves))
                .collect(Collectors.joining("  |  "));
        return String.format("|  %d  |  %s  |", rowNumber, rowContent);
    }

    private String squareToMarkdown(String square, String position, List<String> validMoves) {
        String selectedSquare = board.getSelectedSquare();
        if (selectedSquare != null && validMoves.stream().anyMatch(move -> move.startsWith(selectedSquare) && move.endsWith(position))) {
            String squareSymbol = square != null ? square : "_";
            return String.format("[%s](http://localhost:8080/api/chess/play?move=%s%s)", squareSymbol, selectedSquare, position);
        }
        if (validMoves.stream().anyMatch(move -> move.startsWith(position))) {
            return String.format("[%s](http://localhost:8080/api/chess/select?square=%s)", square, position);
        }
        return square == null ? " " : square;
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
