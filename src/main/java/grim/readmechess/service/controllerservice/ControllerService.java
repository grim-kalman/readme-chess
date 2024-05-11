package grim.readmechess.service.controllerservice;

import grim.readmechess.service.engineservice.dto.EngineResponseDTO;
import grim.readmechess.model.chessboard.Board;
import grim.readmechess.utils.printer.FenBoardPrinter;
import grim.readmechess.utils.printer.MarkdownBoardPrinter;
import grim.readmechess.service.engineservice.EngineService;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static grim.readmechess.utils.common.Constants.PATH_TO_ENGINE;

@Service
public class ControllerService {

    private final Board board;
    private final FenBoardPrinter fenBoardPrinter;
    private final MarkdownBoardPrinter markdownBoardPrinter;
    private final EngineService engineService;

    public ControllerService(Board board, FenBoardPrinter fenBoardPrinter, MarkdownBoardPrinter markdownBoardPrinter, EngineService engineService) {
        this.board = board;
        this.fenBoardPrinter = fenBoardPrinter;
        this.markdownBoardPrinter = markdownBoardPrinter;
        this.engineService = engineService;
        try {
            this.engineService.startEngine(PATH_TO_ENGINE);
        } catch (IOException e) {
            throw new RuntimeException("Error starting engine service", e);
        }
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
