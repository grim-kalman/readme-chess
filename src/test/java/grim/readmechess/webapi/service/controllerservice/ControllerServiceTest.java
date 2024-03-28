package grim.readmechess.webapi.service.controllerservice;

import grim.readmechess.webapi.model.chessboard.Board;
import grim.readmechess.webapi.model.chessboard.BoardState;
import grim.readmechess.webapi.service.engineservice.EngineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerServiceTest {

    private ControllerService controllerService;

    @BeforeEach
    public void setUp() {
        Board board = new Board(new BoardState());
        controllerService = new ControllerService(board, new EngineService());
    }

    @Test
    void getMoveFromEngineReturnsBestMove() {
        String fen = "rnbqkbnr/pp3ppp/8/2pPp3/8/5N2/PPPP1PPP/RNBQKB1R w KQkq c6 0 4";
        assertEquals("f3e5", controllerService.getMoveFromEngine(fen));
    }

    @Test
    void makeMoveShouldUpdateBoardState() {
        String playerMove = "e2e4";
        String expectedFen = "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2";
        controllerService.makeMove(playerMove);
        String actualFen = controllerService.boardPrinter.printFEN();
        assertEquals(expectedFen, actualFen);
    }
}
