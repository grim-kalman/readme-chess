package grim.readmechess.webapi.service.engineservice;

import grim.readmechess.webapi.dto.EngineResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class EngineServiceTest {
    private EngineService engineService;
    private String pathToEngine = "src/main/resources/stockfish.exe";

    @BeforeEach
    void setUp() {
        engineService = new EngineService();
    }

    @Test
    void shouldStartEngine() {
        try {
            engineService.startEngine(pathToEngine);
        } catch (IOException | EngineServiceException e) {
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
            String bestMove = engineService.getBestMove(1000);
            assertEquals("e2e4", bestMove);
        } catch (IOException | EngineServiceException e) {
            fail("Failed to get best move: " + e.getMessage());
        }
    }

    @Test
    void shouldGetEvaluation() {
        try {
            engineService.startEngine(pathToEngine);
            Double evaluation = engineService.getEvaluation(1000);
            assertEquals(0.09, evaluation);
        } catch (IOException | EngineServiceException e) {
            fail("Failed to get evaluation: " + e.getMessage());
        }
    }

    @Test
    void shouldGetEngineResponse() {
        try {
            engineService.startEngine(pathToEngine);
            EngineResponseDTO engineResponseDTO = engineService.getEngineResponse(1000);
            assertEquals("e2e4", engineResponseDTO.bestMove());
            assertEquals(0.09, engineResponseDTO.evaluation());
        } catch (IOException | EngineServiceException e) {
            fail("Failed to get engine response: " + e.getMessage());
        }
    }
}