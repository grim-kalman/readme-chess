package grim.readmechess.webapi.service.engineservice;

import grim.readmechess.webapi.dto.EngineResponseDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Optional;

@Service
public class EngineService {
    private Process engineProcess;
    private BufferedReader output;
    private PrintWriter input;

    public void startEngine(String pathToEngine) throws IOException, EngineServiceException {
        ProcessBuilder processBuilder = new ProcessBuilder(pathToEngine);
        engineProcess = processBuilder.start();
        output = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
        input = new PrintWriter(engineProcess.getOutputStream(), true);
        if (!isEngineReady()) {
            throw new EngineServiceException("Engine failed to respond with 'readyok' during startup");
        }
    }

    private boolean isEngineReady() throws EngineServiceException {
        sendCommand("isready\n");
        try {
            boolean ready = readEngineOutput("readyok", 100).isPresent();
            if (!ready) {
                throw new EngineServiceException("Engine did not become ready within the expected time.");
            }
            return ready;
        } catch (IOException e) {
            throw new EngineServiceException("IO error while checking if the engine is ready: " + e.getMessage());
        }
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

    public void updateEngineState(String FEN) {
        sendCommand("position fen " + FEN + "\n");
    }

    public EngineResponseDTO getEngineResponse(long timeoutMillis) throws EngineServiceException {
        try {
            String bestMove = getBestMove(timeoutMillis);
            Double evaluation = getEvaluation(timeoutMillis);
            return new EngineResponseDTO(bestMove, evaluation);
        } catch (IOException e) {
            throw new EngineServiceException("Failed to get response from engine within the specified timeout: " + e.getMessage());
        }
    }

    String getBestMove(long timeoutMillis) throws IOException, EngineServiceException {
        sendCommand("go depth 16\n");
        return readEngineOutput("bestmove", timeoutMillis)
                .map(line -> line.split(" ")[1])
                .orElseThrow(() -> new EngineServiceException("Best move not found"));
    }

    Double getEvaluation(long timeoutMillis) throws IOException, EngineServiceException {
        sendCommand("eval\n");
        return readEngineOutput("NNUE evaluation", timeoutMillis)
                .map(line -> line.replaceAll("[^0-9.-]", ""))
                .map(Double::parseDouble)
                .orElseThrow(() -> new EngineServiceException("Evaluation not found"));
    }

    private Optional<String> readEngineOutput(String expectedOutputStart, long timeoutMillis) throws IOException {
        String line;

        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < timeoutMillis) {
            line = output.readLine();
            if (line != null && line.startsWith(expectedOutputStart)) {
                return Optional.of(line);
            }
        }
        return Optional.empty();
    }
}
