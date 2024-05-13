package grim.readmechess.service.engineservice;

import grim.readmechess.service.engineservice.dto.EngineResponseDTO;
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
        Process engineProcess = new ProcessBuilder(pathToEngine).start();
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
        String bestMove = getBestMove();
        return new EngineResponseDTO(bestMove, getEvaluation(), "(none)".equals(bestMove));
    }

    String getBestMove() {
        sendCommand("go depth 8");
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
