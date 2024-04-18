package grim.readmechess.webapi.service.engineservice;

import grim.readmechess.webapi.dto.EngineResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static grim.readmechess.utils.Constants.PATH_TO_ENGINE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EngineServiceTest {
    @Autowired
    private EngineService engineService;

    private final String pathToEngine = PATH_TO_ENGINE;

    @Test
    void shouldStartEngine() {
        try {
            engineService.startEngine(pathToEngine);
        } catch (IOException e) {
            fail("Failed to start engine: " + e.getMessage());
        }
    }

    @Test
    void shouldStopEngine() {
        try {
            engineService.startEngine(pathToEngine);
            engineService.stopEngine();
        } catch (IOException | EngineServiceException e) {
            fail("Failed to stop engine: " + e.getMessage());
        }
    }

    @Test
    void shouldGetBestMove() {
        try {
            engineService.startEngine(pathToEngine);
            String bestMove = engineService.getBestMove();
            assertEquals("e2e4", bestMove);
        } catch (IOException | EngineServiceException e) {
            fail("Failed to get best move: " + e.getMessage());
        }
    }

    @Test
    void shouldGetEvaluation() {
        try {
            engineService.startEngine(pathToEngine);
            Double evaluation = engineService.getEvaluation();
            assertEquals(0.09, evaluation);
        } catch (IOException | EngineServiceException e) {
            fail("Failed to get evaluation: " + e.getMessage());
        }
    }

    @Test
    void shouldGetEngineResponse() {
        try {
            engineService.startEngine(pathToEngine);
            EngineResponseDTO engineResponseDTO = engineService.getEngineResponse();
            assertEquals("e2e4", engineResponseDTO.bestMove());
            assertEquals(0.09, engineResponseDTO.evaluation());
        } catch (IOException | EngineServiceException e) {
            fail("Failed to get engine response: " + e.getMessage());
        }
    }

    @Test
    void shouldGetValidMoves() {
        try {
            engineService.startEngine(pathToEngine);
            engineService.updateEngineState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            Optional<List<String>> validMoves = engineService.getValidMoves();
            assertTrue(validMoves.isPresent());
        } catch (IOException e) {
            fail("Failed to get valid moves: " + e.getMessage());
        }
    }
}