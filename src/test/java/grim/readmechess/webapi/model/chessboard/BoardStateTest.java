package grim.readmechess.webapi.model.chessboard;

import grim.readmechess.webapi.model.chesspieces.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static grim.readmechess.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardStateTest {

    private BoardState boardState;

    @BeforeEach
    void setUp() {
        boardState = new BoardState();
    }

    @Test
    void shouldUpdateActiveColor() {
        boardState.updateActiveColor();
        assertEquals(BLACK, boardState.getActiveColor());
        boardState.updateActiveColor();
        assertEquals(WHITE, boardState.getActiveColor());
    }

    @Test
    void shouldUpdateCastlingRights() {
        boardState.updateCastlingRights("e1");
        assertEquals("kq", boardState.getCastlingAvailability());
    }

    @Test
    void shouldResetEnPassantTarget() {
        boardState.resetEnPassantTarget();
        assertEquals("-", boardState.getEnPassantTarget());
    }

    @Test
    void shouldUpdateHalfMoveClock() {
        List<Piece> pieces = new ArrayList<>();
        boardState.updateHalfMoveClock("e4", pieces);
        assertEquals(1, boardState.getHalfMoveClock());
    }

    @Test
    void shouldHandleEnPassantTarget() {
        boardState.handleEnPassantTarget("e2", "e4");
        assertEquals("e6", boardState.getEnPassantTarget());
    }
}
