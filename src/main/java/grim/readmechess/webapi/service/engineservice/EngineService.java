package grim.readmechess.webapi.service.engineservice;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@Service
public class EngineService {
    private Process engineProcess;
    private BufferedReader processReader;
    private PrintWriter processWriter;

    public void startEngine(String pathToEngine) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(pathToEngine);
        engineProcess = processBuilder.start();
        processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
        processWriter = new PrintWriter(engineProcess.getOutputStream(), true);
    }

    public void sendCommand(String command) {
        processWriter.println(command);
    }

    public String getEngineOutput() throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = processReader.readLine()) != null) {
            builder.append(line).append(" ");
            if (line.contains("readyok")) {
                break;
            }
        }
        return builder.toString();
    }

    public void stopEngine() {
        processWriter.println("quit");
        processWriter.close();
        try {
            processReader.close();
            engineProcess.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
