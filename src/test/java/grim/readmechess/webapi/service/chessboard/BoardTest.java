package grim.readmechess.webapi.service.chessboard;

import grim.readmechess.webapi.service.chesspieces.Piece;
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
        assertEquals(WHITE, board.getBoardState().getActiveColor());
        board.makeMove("e2e4");
        assertEquals(BLACK, board.getBoardState().getActiveColor());
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
