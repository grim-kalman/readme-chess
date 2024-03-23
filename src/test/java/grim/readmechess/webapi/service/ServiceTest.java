package grim.readmechess.webapi.service;

import grim.readmechess.webapi.service.chessboard.Board;
import grim.readmechess.webapi.service.chessboard.BoardState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ServiceTest {

    private Service service;

    @BeforeEach
    public void setup() {
        Board board = new Board(new BoardState());
        service = new Service(board);
    }

    @Test
    void getMoveFromEngineReturnsBestMove() {
        String fen = "rnbqkbnr/pp3ppp/8/2pPp3/8/5N2/PPPP1PPP/RNBQKB1R w KQkq c6 0 4";
        String engineMove = service.getMoveFromEngine(fen);
        assertEquals("f3e5", engineMove);
    }

    @Test
    void makeMoveShouldUpdateBoardState() {
        String playerMove = "e2e4";
        service.makeMove(playerMove);

    }
}
