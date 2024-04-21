package grim.readmechess.webapi.model.chesspieces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static grim.readmechess.utils.Constants.WHITE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PieceTest {
    private Piece piece;

    @BeforeEach
    public void setup() {
        piece = new King(WHITE, "e1");
    }

    @Test
    void fenSymbolIsReturnedCorrectly() {
        assertEquals("K", piece.getSymbol());
    }

    @Test
    void positionIsReturnedCorrectly() {
        assertEquals("e1", piece.getPosition());
    }

    @Test
    void positionIsSetCorrectly() {
        piece.setPosition("e2");
        assertEquals("e2", piece.getPosition());
    }

}
