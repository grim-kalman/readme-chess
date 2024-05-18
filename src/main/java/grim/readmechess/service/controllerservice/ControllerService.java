package grim.readmechess.service.controllerservice;

import grim.readmechess.service.engineservice.dto.EngineResponseDTO;
import grim.readmechess.model.chessboard.Board;
import grim.readmechess.utils.printer.FenBoardPrinter;
import grim.readmechess.utils.printer.MarkdownBoardPrinter;
import grim.readmechess.service.engineservice.EngineService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ControllerService {

    private final Board board;
    private final FenBoardPrinter fenBoardPrinter;
    private final MarkdownBoardPrinter markdownBoardPrinter;
    private final EngineService engineService;

    @Value("${chess.engine.path}")
    private String enginePath;

    @PostConstruct
    public void initialize() throws IOException {
        engineService.startEngine(enginePath);
    }

    public void play(String move) {
        makeMove(move);
        makeMove(getEngineResponse().bestMove());
    }

    public void select(String square) {
        board.selectSquare(square);
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
