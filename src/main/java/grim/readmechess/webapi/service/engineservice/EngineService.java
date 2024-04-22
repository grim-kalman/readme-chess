package grim.readmechess.webapi.service.engineservice;

import grim.readmechess.webapi.dto.EngineResponseDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service
public class EngineService {
    private BufferedReader output;
    private PrintWriter input;

    private Double lastEvaluation;

    public void startEngine(String pathToEngine) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(pathToEngine);
        Process engineProcess = processBuilder.start();
        output = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
        input = new PrintWriter(engineProcess.getOutputStream(), true);
        lastEvaluation = getEvaluation();
    }

    public void sendCommand(String command) {
        input.println(command);
    }

    public void updateEngineState(String fen) {
        sendCommand("position fen " + fen);
    }

    public EngineResponseDTO getEngineResponse() {
        return new EngineResponseDTO(getBestMove(), getEvaluation());
    }

    String getBestMove() {
        sendCommand("go depth 16");
        return readEngineOutput("bestmove")
                .map(line -> line.split(" ")[1])
                .orElseThrow(() -> new RuntimeException("Best move not found"));
    }

    Double getEvaluation() {
        sendCommand("eval");
        return lastEvaluation = readEngineOutput("NNUE evaluation")
                .map(line -> line.replaceAll("[^0-9.-]", ""))
                .map(Double::parseDouble)
                .orElse(lastEvaluation);
    }

    public List<String> getValidMoves() {
        sendCommand("go perft 1");
        return output.lines()
                .takeWhile(line -> !line.startsWith("Nodes searched"))
                .filter(line -> line.contains(":"))
                .map(line -> line.split(":")[0])
                .toList();
    }

    private Optional<String> readEngineOutput(String expectedOutputStart) {
        return output.lines()
                .takeWhile(line -> !line.contains("Final evaluation: none (in check)"))
                .filter(line -> line.startsWith(expectedOutputStart))
                .findFirst();
    }
}
