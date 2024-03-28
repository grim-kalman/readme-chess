package grim.readmechess.webapi.service.controllerservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grim.readmechess.webapi.model.chessboard.Board;
import grim.readmechess.webapi.model.chessboard.BoardPrinter;
import grim.readmechess.webapi.dto.EngineResponseDTO;
import grim.readmechess.webapi.service.engineservice.EngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
public class ControllerService {

    final Board board;
    final BoardPrinter boardPrinter;
    final EngineService engineService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(ControllerService.class);

    public ControllerService(Board board, EngineService engineService) {
        this.board = board;
        this.boardPrinter = new BoardPrinter(board);
        this.engineService = engineService;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public void makeMove(String playerMove) {

        //1. MakeMove
        board.makeMove(playerMove);

        //2. Stockfish and do it asynchronously?
        String engineMove = getMoveFromEngine(boardPrinter.printFEN());

        //3. MakeMove Update board state
        board.makeMove(engineMove);

        //4. Get all possible moves from the new position

        //5. Update the readme with all the new links and pieces

        //6. Make API call to GitHub to commit a new readme

    }

    String getMoveFromEngine(String fen) {
        String url = String.format("https://stockfish.online/api/s/v2.php?fen=%s&depth=15", fen);
        try {
            String response = restTemplate.getForObject(url, String.class);
            EngineResponseDTO engineResponse = objectMapper.readValue(response, EngineResponseDTO.class);
            return engineResponse.bestmove().split(" ")[1];
        } catch (RestClientException | JsonProcessingException e) {
            log.error("Error fetching data from the engine: ", e);
            return null;
        }
    }

}
