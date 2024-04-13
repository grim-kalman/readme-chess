package grim.readmechess.webapi.service.engineservice;

import grim.readmechess.webapi.dto.EngineResponseDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@Service
public class EngineService {
    private Process engineProcess;
    private BufferedReader output;
    private PrintWriter input;
    private final Object lock = new Object();

    public synchronized void startEngine(String pathToEngine) throws IOException, InterruptedException, EngineServiceException {
        ProcessBuilder processBuilder = new ProcessBuilder(pathToEngine);
        engineProcess = processBuilder.start();
        output = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
        input = new PrintWriter(engineProcess.getOutputStream(), true);

        try {
            waitForEngine();
        } catch (IOException e) {
            throw new EngineServiceException("Engine failed to respond with 'readyok' during startup", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EngineServiceException("Thread was interrupted during engine startup", e);
        }
    }

    public void sendCommand(String command) {
        synchronized (lock) {
            System.out.println("DEBUG: Sending command: " + command);  // Log command
            input.write(command);
            input.flush();
        }
    }

    private void waitForEngine() throws IOException, InterruptedException {
        sendCommand("isready\n");
        String response;
        while (true) {
            response = output.readLine();
            System.out.println("DEBUG: Waiting for 'readyok', got: " + response);  // Log responses during wait
            if (response.equals("readyok")) {
                break;
            }
            Thread.sleep(10);
        }
    }

    public void stopEngine() {
        engineProcess.destroy();
    }

    public EngineResponseDTO getEngineResponse(long timeoutMillis) throws EngineServiceException {
        try {
            String bestMove = getBestMove(timeoutMillis);
            Double evaluation = getEvaluation(timeoutMillis);
            return new EngineResponseDTO(bestMove, evaluation);
        } catch (IOException | InterruptedException e) {
            throw new EngineServiceException("Failed to get response from engine within the specified timeout", e);
        }
    }

    public synchronized String getBestMove(long timeoutMillis) throws IOException, InterruptedException {
        sendCommand("go depth 16\n");
        return readEngineOutput("bestmove", timeoutMillis).split(" ")[1];
    }

    public synchronized Double getEvaluation(long timeoutMillis) throws IOException, InterruptedException {
        sendCommand("eval\n");
        return Double.valueOf(readEngineOutput("NNUE evaluation", timeoutMillis).split(" ")[2]);
    }

    private String readEngineOutput(String expectedOutputStart, long timeoutMillis) throws IOException, InterruptedException {
        String line;
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < timeoutMillis) {
            if (output.ready()) {  // Check if there's something to read
                line = output.readLine();
                System.out.println("DEBUG: Read line: " + line);  // Print out each line read from the CLI
                if (line != null && line.startsWith(expectedOutputStart)) {
                    System.out.println("DEBUG: Found expected start: " + line);  // Indicate when the expected line is found
                    return line;
                }
            }
            Thread.sleep(10); // Sleep to avoid high CPU usage
        }
        throw new IOException("Engine did not respond with the expected output '" + expectedOutputStart +"' within the timeout: " + timeoutMillis + "ms");
    }
}
