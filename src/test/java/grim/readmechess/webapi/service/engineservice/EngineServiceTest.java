package grim.readmechess.webapi.service.engineservice;

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
        } catch (IOException | InterruptedException | EngineServiceException e) {
            fail("Failed to start engine");
        }
    }

    @Test
    void shouldStopEngine() {
        try {
            engineService.startEngine(pathToEngine);
            engineService.stopEngine();
        } catch (IOException | InterruptedException | EngineServiceException e) {
            fail("Failed to stop engine");
        }
    }

    @Test
    void shouldGetBestMove() {
        try {
            engineService.startEngine(pathToEngine);
            String bestMove = engineService.getBestMove(1000);
            assertNotNull(bestMove);
        } catch (IOException | InterruptedException | EngineServiceException e) {
            fail("Failed to get best move", e);
        }
    }
}