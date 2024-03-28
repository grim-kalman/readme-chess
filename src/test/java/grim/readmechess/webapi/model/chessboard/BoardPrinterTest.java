package grim.readmechess.webapi.model.chessboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardPrinterTest {

    private Board board;
    private BoardPrinter boardPrinter;

    @BeforeEach
    void setUp() {
        BoardState boardState = new BoardState();
        board = new Board(boardState);
        boardPrinter = new BoardPrinter(board);
    }

    @Test
    void shouldPrintStartFEN() {
        String fen = boardPrinter.printFEN();
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fen);
    }

    @Test
    void shouldPrintUpdatedFEN() {
        board.makeMove("e2e4");
        board.makeMove("e7e5");
        board.makeMove("g1f3");
        board.makeMove("d7d5");
        board.makeMove("e4d5");
        board.makeMove("c7c5");
        String fen = boardPrinter.printFEN();
        assertEquals("rnbqkbnr/pp3ppp/8/2pPp3/8/5N2/PPPP1PPP/RNBQKB1R w KQkq c6 0 4", fen);
    }

    @Test
    void shouldPrintSVG() {
        board.makeMove("e2e4");
        board.makeMove("e7e5");
        board.makeMove("g1f3");
        board.makeMove("d7d5");
        board.makeMove("e4d5");
        board.makeMove("c7c5");
        String svg = boardPrinter.printSVG();
        assertEquals("<svg viewBox=\"0 0 400 400\" xmlns=\"http://www.w3.org/2000/svg\"><rect x=\"40\" y=\"40\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"80\" y=\"40\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"120\" y=\"40\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"160\" y=\"40\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"200\" y=\"40\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"240\" y=\"40\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"280\" y=\"40\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"320\" y=\"40\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"40\" y=\"80\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"80\" y=\"80\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"120\" y=\"80\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"160\" y=\"80\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"200\" y=\"80\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"240\" y=\"80\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"280\" y=\"80\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"320\" y=\"80\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"40\" y=\"120\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"80\" y=\"120\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"120\" y=\"120\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"160\" y=\"120\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"200\" y=\"120\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"240\" y=\"120\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"280\" y=\"120\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"320\" y=\"120\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"40\" y=\"160\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"80\" y=\"160\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"120\" y=\"160\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"160\" y=\"160\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"200\" y=\"160\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"240\" y=\"160\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"280\" y=\"160\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"320\" y=\"160\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"40\" y=\"200\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"80\" y=\"200\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"120\" y=\"200\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"160\" y=\"200\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"200\" y=\"200\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"240\" y=\"200\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"280\" y=\"200\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"320\" y=\"200\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"40\" y=\"240\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"80\" y=\"240\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"120\" y=\"240\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"160\" y=\"240\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"200\" y=\"240\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"240\" y=\"240\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"280\" y=\"240\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"320\" y=\"240\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"40\" y=\"280\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"80\" y=\"280\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"120\" y=\"280\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"160\" y=\"280\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"200\" y=\"280\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"240\" y=\"280\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"280\" y=\"280\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"320\" y=\"280\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"40\" y=\"320\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"80\" y=\"320\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"120\" y=\"320\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"160\" y=\"320\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"200\" y=\"320\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"240\" y=\"320\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><rect x=\"280\" y=\"320\" width=\"40\" height=\"40\" fill=\"#B58863\"/><rect x=\"320\" y=\"320\" width=\"40\" height=\"40\" fill=\"#D8AF87\"/><text x=\"60\" y=\"20\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">a</text><text x=\"60\" y=\"380\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">a</text><text x=\"100\" y=\"20\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">b</text><text x=\"100\" y=\"380\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">b</text><text x=\"140\" y=\"20\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">c</text><text x=\"140\" y=\"380\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">c</text><text x=\"180\" y=\"20\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">d</text><text x=\"180\" y=\"380\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">d</text><text x=\"220\" y=\"20\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">e</text><text x=\"220\" y=\"380\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">e</text><text x=\"260\" y=\"20\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">f</text><text x=\"260\" y=\"380\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">f</text><text x=\"300\" y=\"20\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">g</text><text x=\"300\" y=\"380\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">g</text><text x=\"340\" y=\"20\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">h</text><text x=\"340\" y=\"380\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">h</text><text x=\"20\" y=\"340\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">1</text><text x=\"380\" y=\"340\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">1</text><text x=\"20\" y=\"300\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">2</text><text x=\"380\" y=\"300\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">2</text><text x=\"20\" y=\"260\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">3</text><text x=\"380\" y=\"260\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">3</text><text x=\"20\" y=\"220\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">4</text><text x=\"380\" y=\"220\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">4</text><text x=\"20\" y=\"180\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">5</text><text x=\"380\" y=\"180\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">5</text><text x=\"20\" y=\"140\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">6</text><text x=\"380\" y=\"140\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">6</text><text x=\"20\" y=\"100\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">7</text><text x=\"380\" y=\"100\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">7</text><text x=\"20\" y=\"60\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">8</text><text x=\"380\" y=\"60\" dominant-baseline=\"middle\" fill=\"grey\" font-size=\"15\" text-anchor=\"middle\">8</text><text x=\"220\" y=\"346\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♚</text><text x=\"220\" y=\"66\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♚</text><text x=\"180\" y=\"346\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♛</text><text x=\"180\" y=\"66\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♛</text><text x=\"60\" y=\"346\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♜</text><text x=\"340\" y=\"346\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♜</text><text x=\"60\" y=\"66\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♜</text><text x=\"340\" y=\"66\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♜</text><text x=\"100\" y=\"346\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♞</text><text x=\"260\" y=\"266\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♞</text><text x=\"100\" y=\"66\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♞</text><text x=\"300\" y=\"66\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♞</text><text x=\"140\" y=\"346\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♝</text><text x=\"260\" y=\"346\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♝</text><text x=\"140\" y=\"66\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♝</text><text x=\"260\" y=\"66\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♝</text><text x=\"60\" y=\"306\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"60\" y=\"106\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"100\" y=\"306\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"100\" y=\"106\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"140\" y=\"306\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"140\" y=\"186\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"180\" y=\"306\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"180\" y=\"186\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"220\" y=\"186\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"260\" y=\"306\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"260\" y=\"106\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"300\" y=\"306\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"300\" y=\"106\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"340\" y=\"306\" dominant-baseline=\"middle\" fill=\"white\" font-size=\"36\" text-anchor=\"middle\">♟</text><text x=\"340\" y=\"106\" dominant-baseline=\"middle\" fill=\"black\" font-size=\"36\" text-anchor=\"middle\">♟</text></svg>", svg);
    }
}