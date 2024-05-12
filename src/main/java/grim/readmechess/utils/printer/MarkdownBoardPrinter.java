package grim.readmechess.utils.printer;

import grim.readmechess.model.chessboard.Board;
import grim.readmechess.utils.validator.MoveValidator;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MarkdownBoardPrinter extends BoardPrinter {

    private final MoveValidator moveValidator;

    public MarkdownBoardPrinter(Board board, MoveValidator moveValidator) {
        super(board);
        this.moveValidator = moveValidator;
    }

    @Override
    public String print() {
        return convertBoardToMarkdown();
    }

    private String convertBoardToMarkdown() {
        String header = "|     |  a  |  b  |  c  |  d  |  e  |  f  |  g  |  h  |\n";
        String separator = "|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|\n";
        String boardRepresentation = formatBoardRepresentation(createBoardRepresentation());
        return header + separator + boardRepresentation;
    }

    private String formatBoardRepresentation(String[][] boardRepresentation) {
        return IntStream.range(0, boardRepresentation.length)
                .mapToObj(i -> formatMarkdownRow(boardRepresentation[i], boardRepresentation.length - i))
                .collect(Collectors.joining("\n")) + "\n";
    }

    private String formatMarkdownRow(String[] row, int rowNumber) {
        return "|  **" + rowNumber + "**  |  " + convertRowToMarkdown(row, rowNumber) + "  |";
    }

    private String convertRowToMarkdown(String[] row, int rowNumber) {
        return IntStream.range(0, row.length)
                .mapToObj(i -> convertSquareToMarkdown(row[i], (char) ('a' + i) + Integer.toString(rowNumber)))
                .collect(Collectors.joining("  |  "));
    }

    private String convertSquareToMarkdown(String square, String position) {
        String selectedSquare = board.getSelectedSquare();
        String squareSymbol = formatSquareSymbol(square);

        if (selectedSquare != null && moveValidator.isValid(selectedSquare + position)) {
            return createMarkdownLink(squareSymbol, "http://localhost:8080/api/chess/play?move=" + selectedSquare + position);
        }
        if (moveValidator.isStartOfValidMove(position)) {
            return createMarkdownLink(squareSymbol, "http://localhost:8080/api/chess/select?square=" + position);
        }
        if (square == null) {
            return " ";
        }
        if (Character.isUpperCase(square.charAt(0))) {
            return createMarkdownLink(squareSymbol, "https://github.com/grim-kalman");
        }
        return squareSymbol;
    }

    private String formatSquareSymbol(String square) {
        if (square == null) {
            return "_";
        }
        if (Character.isLowerCase(square.charAt(0))) {
            return "_" + square + "_";
        }
        return "**" + square + "**";
    }

    private String createMarkdownLink(String text, String url) {
        return "[" + text + "](" + url + ")";
    }

}
