package grim.readmechess.webapi.service.controllerservice;

import grim.readmechess.webapi.model.chessboard.Board;
import grim.readmechess.webapi.model.chessboard.BoardPrinter;
import grim.readmechess.webapi.model.chessboard.BoardState;
import grim.readmechess.webapi.service.engineservice.EngineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ControllerServiceTest {

    @Autowired
    private ControllerService controllerService;

    @Test
    void getMoveFromEngineReturnsExpectedMove() throws ControllerServiceException {
        String move = controllerService.getMoveFromEngine();
        assertEquals("e2e4", move);
    }
}