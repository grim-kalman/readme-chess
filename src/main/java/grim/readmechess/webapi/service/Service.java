package grim.readmechess.webapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grim.readmechess.webapi.service.chessboard.Board;
import grim.readmechess.webapi.service.chessboard.BoardPrinter;
import grim.readmechess.webapi.service.engineResponseDTO.EngineResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
public class Service {

    private final Board board;
    private final BoardPrinter boardPrinter;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(Service.class);

    public Service(Board board) {
        this.board = board;
        this.boardPrinter = new BoardPrinter(board);
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public void makeMove(String playerMove) {

        //1. MakeMove
        board.makeMove(playerMove);

        //2. Get best move from API or Stockfish and do it asynchronously?
        String engineMove = getMoveFromEngine(boardPrinter.printFEN());

        //3. MakeMove Update board state
        board.makeMove(engineMove);

        //4. Get all possible moves from the new position

        //5. Update the readme with all the new links and pieces

        //6. Make API call to GitHub to commit a new readme

    }

    String getMoveFromEngine(String fen) {
        String url = String.format("https://stockfish.online/api/s/v2.php?fen=%s&depth=15", fen);
        log.info("GET request URL: {}", url);
        try {
            String response = restTemplate.getForObject(url, String.class);
            log.info("Received response: {}", response);
            String bestMove = objectMapper.readValue(response, EngineResponseDTO.class).bestmove();
            return bestMove.split(" ")[1];
        } catch (RestClientException | JsonProcessingException e) {
            log.error("Error fetching data from the engine: ", e);
            return null;
        }
    }

}
