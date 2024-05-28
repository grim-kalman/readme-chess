package grim.readmechess.service.engineservice;

import grim.readmechess.config.AppConfig;
import grim.readmechess.service.engineservice.dto.EngineResponseDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EngineService {

    private final AppConfig appConfig;
    private Process engineProcess;
    private BufferedReader output;
    private PrintWriter input;
    private Double lastEvaluation;

    public void startEngine() throws IOException {
        engineProcess = new ProcessBuilder(appConfig.getEnginePath()).start();
        output = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
        input = new PrintWriter(engineProcess.getOutputStream(), true);
        lastEvaluation = getEvaluation();
    }

    public void stopEngine() throws IOException {
        sendCommand("quit");
        output.close();
        input.close();
        engineProcess.destroy();
    }

    public void sendCommand(String command) {
        input.println(command);
    }

    public void updateEngineState(String fen) {
        sendCommand("position fen " + fen);
    }

    public EngineResponseDTO getEngineResponse() {
        String bestMove = getBestMove().orElseThrow(() -> new RuntimeException("No best move found"));
        return new EngineResponseDTO(bestMove, getEvaluation(), "(none)".equals(bestMove));
    }

    private Optional<String> getBestMove() {
        sendCommand("go depth 16");
        return readEngineOutput("bestmove")
                .map(line -> line.split(" ")[1]);
    }

    private Double getEvaluation() {
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
