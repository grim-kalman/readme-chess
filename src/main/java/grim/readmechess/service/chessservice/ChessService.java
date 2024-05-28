package grim.readmechess.service.chessservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import grim.readmechess.model.chessboard.Board;
import grim.readmechess.service.engineservice.EngineService;
import grim.readmechess.service.engineservice.dto.EngineResponseDTO;
import grim.readmechess.service.githubservice.GithubService;
import grim.readmechess.utils.printer.FenBoardPrinter;
import grim.readmechess.utils.printer.MarkdownBoardPrinter;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ChessService {

    private final Board board;
    private final FenBoardPrinter fenBoardPrinter;
    private final MarkdownBoardPrinter markdownBoardPrinter;
    private final GithubService githubService;
    private final EngineService engineService;

    @PostConstruct
    public void init() throws IOException {
        startService();
    }

    private void startService() throws IOException {
        engineService.startEngine();
        githubService.updateReadme(this);
    }

    public void play(String move) throws JsonProcessingException {
        makeMove(move);
        makeMove(getEngineResponse().bestMove());
        githubService.updateReadme(this);
    }

    public void select(String square) throws JsonProcessingException {
        board.selectSquare(square);
        githubService.updateReadme(this);
    }

    public void newGame() throws IOException {
        board.reset();
        engineService.stopEngine();
        startService();
    }

    public String printBoard() {
        return markdownBoardPrinter.print();
    }

    private EngineResponseDTO getEngineResponse() {
        return engineService.getEngineResponse();
    }

    private void makeMove(String move) {
        board.makeMove(move);
        engineService.updateEngineState(fenBoardPrinter.print());
    }
}
