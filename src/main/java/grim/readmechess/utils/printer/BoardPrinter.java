package grim.readmechess.utils.printer;

import grim.readmechess.model.chessboard.Board;
import grim.readmechess.model.chesspieces.Piece;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static grim.readmechess.utils.common.Utils.columnToIndex;
import static grim.readmechess.utils.common.Utils.rowToIndex;

@Slf4j
public abstract class BoardPrinter {

    protected final Board board;

    @Autowired
    protected BoardPrinter(Board board) {
        this.board = board;
    }

    protected String[][] createBoardRepresentation() {
        String[][] boardRepresentation = new String[8][8];
        board.getPieceService().getPieces().forEach((position, piece) -> populateBoardRepresentation(position, piece, boardRepresentation));
        return boardRepresentation;
    }

    private void populateBoardRepresentation(String position, Piece piece, String[][] boardRepresentation) {
        int col = columnToIndex(position.charAt(0));
        int row = rowToIndex(position.charAt(1));
        if (piece == null) {
            log.error("Piece at position {} is null", position);
        } else {
            log.info("Piece at position {}: {}", position, piece.getSymbol());
            boardRepresentation[row][col] = piece.getSymbol();
        }
    }

    protected abstract String print();
}
