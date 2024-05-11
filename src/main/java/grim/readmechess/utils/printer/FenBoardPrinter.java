package grim.readmechess.utils.printer;

import grim.readmechess.model.chessboard.Board;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class FenBoardPrinter extends BoardPrinter {

    protected FenBoardPrinter(Board board) {
        super(board);
    }

    @Override
    public String print() {
        return convertBoardToFEN(createBoardRepresentation()) + " " + getBoardStateFEN();
    }

    private String convertBoardToFEN(String[][] boardRepresentation) {
        return Arrays.stream(boardRepresentation)
                .map(this::convertRowToFEN)
                .map(this::replaceConsecutiveOnesWithCount)
                .collect(Collectors.joining("/"));
    }

    private String convertRowToFEN(String[] row) {
        return Arrays.stream(row)
                .map(square -> square == null ? "1" : square)
                .collect(Collectors.joining());
    }

    private String replaceConsecutiveOnesWithCount(String fenRow) {
        return fenRow.replace("11111111", "8")
                .replace("1111111", "7")
                .replace("111111", "6")
                .replace("11111", "5")
                .replace("1111", "4")
                .replace("111", "3")
                .replace("11", "2");
    }

    private String getBoardStateFEN() {
        return board.getBoardState().getActiveColor() + " " +
                board.getBoardState().getCastlingAvailability() + " " +
                board.getBoardState().getEnPassantTarget() + " " +
                board.getBoardState().getHalfMoveClock() + " " +
                board.getBoardState().getFullMoveNumber();
    }
}
