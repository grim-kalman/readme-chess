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

    @Test
    void shouldPrintStartMarkdown() {
        String markdown = boardPrinter.printMarkdown();
        assertEquals("|       |  _a_  |  _b_  |  _c_  |  _d_  |  _e_  |  _f_  |  _g_  |  _h_  |\n" +
                        "| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |\n" +
                        "|  _8_  |   r   |   n   |   b   |   q   |   k   |   b   |   n   |   r   |\n" +
                        "|  _7_  |   p   |   p   |   p   |   p   |   p   |   p   |   p   |   p   |\n" +
                        "|  _6_  |       |       |       |       |       |       |       |       |\n" +
                        "|  _5_  |       |       |       |       |       |       |       |       |\n" +
                        "|  _4_  |       |       |       |       |       |       |       |       |\n" +
                        "|  _3_  |       |       |       |       |       |       |       |       |\n" +
                        "|  _2_  |   P   |   P   |   P   |   P   |   P   |   P   |   P   |   P   |\n" +
                        "|  _1_  |   R   |   N   |   B   |   Q   |   K   |   B   |   N   |   R   |",
                markdown);
    }
}
