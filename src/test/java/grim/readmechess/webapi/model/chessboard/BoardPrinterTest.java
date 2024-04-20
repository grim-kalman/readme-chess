package grim.readmechess.webapi.model.chessboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BoardPrinterTest {

    @Autowired
    private Board board;

    @Autowired
    private BoardPrinter boardPrinter;

    @Test
    void shouldPrintStartFEN() {
        String fen = boardPrinter.printFEN();
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fen);
    }
}
