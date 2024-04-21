package grim.readmechess.webapi.model.chessboard;

import grim.readmechess.webapi.model.chesspieces.Piece;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static grim.readmechess.utils.Utils.convertColumnToIndex;
import static grim.readmechess.utils.Utils.convertRowToIndex;

@Component
public class BoardPrinter {

    private final Board board;

    public BoardPrinter(Board board) {
        this.board = board;
    }

    public String printMarkdown() {
        String[][] boardRepresentation = createBoardRepresentation();
        return convertToMarkdown(boardRepresentation);
    }

    public String printFEN() {
        String[][] boardRepresentation = createBoardRepresentation();
        return convertToFEN(boardRepresentation);
    }

    private String[][] createBoardRepresentation() {
        String[][] boardRepresentation = new String[8][8];
        for (Piece piece : board.getPieces()) {
            updateBoardRepresentation(boardRepresentation, piece);
        }
        return boardRepresentation;
    }

    private void updateBoardRepresentation(String[][] boardRepresentation, Piece piece) {
        String position = piece.getPosition();
        int col = convertColumnToIndex(position.charAt(0));
        int row = convertRowToIndex(position.charAt(1));
        boardRepresentation[row][col] = piece.getSymbol();
    }

    private String convertToMarkdown(String[][] boardRepresentation) {
        String tableHeader = new StringBuilder()
                .append("|       |  _a_  |  _b_  |  _c_  |  _d_  |  _e_  |  _f_  |  _g_  |  _h_  |\n")
                .append("| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |\n")
                .toString();

        String markdownTable = IntStream.range(0, boardRepresentation.length)
                .mapToObj(i -> convertRowToMarkdownNotation(boardRepresentation[i], boardRepresentation.length - i))
                .collect(Collectors.joining("\n"));

        return String.format("%s%s",
                tableHeader,
                markdownTable);
    }

    private String convertRowToMarkdownNotation(String[] row, int rowNumber) {
        String rowContent = Arrays.stream(row)
                .map(square -> square != null ? square : " ")
                .collect(Collectors.joining("   |   "));

        return String.format("|  _%d_  |   %s   |",
                rowNumber,
                rowContent);
    }

    private String convertToFEN(String[][] boardRepresentation) {
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

    private String convertRowToFenNotation(String[] row) {
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
