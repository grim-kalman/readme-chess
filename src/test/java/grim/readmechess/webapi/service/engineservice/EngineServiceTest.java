package grim.readmechess.webapi.service.engineservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EngineServiceTest {
    private EngineService engineService;

    @BeforeEach
    void setUp() {
        engineService = new EngineService();
    }

    @Test
    void startEngine() throws IOException {
        String pathToEngine = "src/main/resources/stockfish.exe";
        engineService.startEngine(pathToEngine);
        engineService.sendCommand("isready");
        String output = engineService.getEngineOutput();
        assertTrue(output.contains("readyok"));
        engineService.stopEngine();
    }
}