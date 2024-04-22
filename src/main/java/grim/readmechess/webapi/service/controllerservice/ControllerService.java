package grim.readmechess.webapi.service.controllerservice;

import grim.readmechess.webapi.dto.EngineResponseDTO;
import grim.readmechess.webapi.model.chessboard.Board;
import grim.readmechess.webapi.model.chessboard.BoardPrinter;
import grim.readmechess.webapi.service.engineservice.EngineService;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static grim.readmechess.utils.Constants.PATH_TO_ENGINE;

@Service
public class ControllerService {

    private final Board board;
    private final BoardPrinter boardPrinter;
    private final EngineService engineService;

    public ControllerService(Board board, BoardPrinter boardPrinter, EngineService engineService) {
        this.board = board;
        this.boardPrinter = boardPrinter;
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
        return boardPrinter.printMarkdown();
    }

    private EngineResponseDTO getEngineResponse() {
        return engineService.getEngineResponse();
    }

    private void makeMove(String move) {
        board.makeMove(move);
        engineService.updateEngineState(boardPrinter.printFEN());
    }
}
