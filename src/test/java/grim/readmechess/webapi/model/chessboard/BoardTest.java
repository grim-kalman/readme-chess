package grim.readmechess.webapi.model.chessboard;

import grim.readmechess.webapi.model.chessboard.Board;
import grim.readmechess.webapi.model.chessboard.BoardState;
import grim.readmechess.webapi.model.chesspieces.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static grim.readmechess.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        BoardState boardState = new BoardState();
        board = new Board(boardState);
    }

    @Test
    void shouldSetupStartingPosition() {
        List<Piece> pieces = board.getPieces();
        assertEquals(32, pieces.size());
    }

    @Test
    void shouldUpdateBoardStateWhenMakingMove() {
        String move = "e2e4";
        board.makeMove(move);
        String activeColor = board.getBoardState().getActiveColor();
        assertEquals(BLACK, activeColor);
    }

    @Test
    void shouldMovePieceWhenMakingMove() {
        String move = "e2e4";
        board.makeMove(move);
        Piece movedPiece = board.getPieces().stream()
                .filter(piece -> piece.getPosition().equals(move.substring(2, 4)))
                .findFirst()
                .orElse(null);
        assertEquals("e4", movedPiece.getPosition());
    }
}
