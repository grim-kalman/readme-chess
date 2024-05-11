package grim.readmechess.utils.printer;

import grim.readmechess.model.chessboard.Board;
import grim.readmechess.model.chesspieces.Piece;

import static grim.readmechess.utils.common.Utils.columnToIndex;
import static grim.readmechess.utils.common.Utils.rowToIndex;

public abstract class BoardPrinter {
    protected final Board board;

    protected BoardPrinter(Board board) {
        this.board = board;
    }

    protected String[][] createBoardRepresentation() {
        String[][] boardRepresentation = new String[8][8];
        board.getPieces().forEach(piece -> populateBoardRepresentation(piece, boardRepresentation));
        return boardRepresentation;
    }

    private void populateBoardRepresentation(Piece piece, String[][] boardRepresentation) {
        int col = columnToIndex(piece.getPosition().charAt(0));
        int row = rowToIndex(piece.getPosition().charAt(1));
        boardRepresentation[row][col] = piece.getSymbol();
    }

    protected abstract String print();
}
