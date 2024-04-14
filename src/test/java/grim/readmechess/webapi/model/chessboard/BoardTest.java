package grim.readmechess.webapi.model.chessboard;

import grim.readmechess.webapi.model.chesspieces.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static grim.readmechess.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
