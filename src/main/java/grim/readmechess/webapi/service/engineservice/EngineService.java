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
    private Process engineProcess;
    private BufferedReader output;
    private PrintWriter input;

    public void startEngine(String pathToEngine) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(pathToEngine);
        engineProcess = processBuilder.start();
        output = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
        input = new PrintWriter(engineProcess.getOutputStream(), true);

    }

    public void sendCommand(String command) {
        input.println(command);
        input.flush();
    }

    public void stopEngine() throws EngineServiceException {
        try {
            output.close();
            input.close();
            engineProcess.destroy();
        } catch (IOException e) {
            throw new EngineServiceException("Failed to gracefully shutdown the engine: " + e.getMessage());
        }
    }

    public void updateEngineState(String fen) {
        sendCommand("position fen " + fen + "\n");
    }

    public EngineResponseDTO getEngineResponse() throws EngineServiceException {
        String bestMove = getBestMove();
        Double evaluation = getEvaluation();
        return new EngineResponseDTO(bestMove, evaluation);
    }

    String getBestMove() throws EngineServiceException {
        sendCommand("go depth 16\n");
        return readEngineOutput("bestmove")
                .map(line -> line.split(" ")[1])
                .orElseThrow(() -> new EngineServiceException("Best move not found"));
    }

    Double getEvaluation() throws EngineServiceException {
        sendCommand("eval\n");

        return readEngineOutput("NNUE evaluation")
                .map(line -> line.replaceAll("[^0-9.-]", ""))
                .map(Double::parseDouble)
                .orElseThrow(() -> new EngineServiceException("Evaluation not found"));
    }

    public Optional<List<String>> getValidMoves() {
        sendCommand("go perft 1\n");

        return Optional.of(output.lines()
                .takeWhile(line -> !line.isBlank())
                .filter(line -> line.contains(":"))
                .map(line -> line.split(":")[0])
                .toList());
    }

    private Optional<String> readEngineOutput(String expectedOutputStart) {
        return output.lines()
                .filter(line -> line.startsWith(expectedOutputStart))
                .findFirst();
    }
}
