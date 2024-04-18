package grim.readmechess.webapi.model.chessboard;

import grim.readmechess.webapi.model.chesspieces.Piece;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static grim.readmechess.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BoardTest {

    @Autowired
    private Board board;

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
    void shouldThrowExceptionWhenMoveIsInvalid() {
        String move = "e2e9";
        assertThrows(IllegalArgumentException.class, () -> board.makeMove(move));
    }
}
