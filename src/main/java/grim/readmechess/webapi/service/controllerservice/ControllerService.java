package grim.readmechess.webapi.service.controllerservice;

import grim.readmechess.webapi.dto.EngineResponseDTO;
import grim.readmechess.webapi.model.chessboard.Board;
import grim.readmechess.webapi.model.chessboard.BoardPrinter;
import grim.readmechess.webapi.service.engineservice.EngineService;
import grim.readmechess.webapi.service.engineservice.EngineServiceException;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static grim.readmechess.utils.Constants.PATH_TO_ENGINE;

@Service
public class ControllerService {

    final Board board;
    final BoardPrinter boardPrinter;
    final EngineService engineService;

    public ControllerService(Board board, BoardPrinter boardPrinter, EngineService engineService) throws ControllerServiceException {
        this.board = board;
        this.boardPrinter = boardPrinter;
        this.engineService = engineService;
        try {
            this.engineService.startEngine(PATH_TO_ENGINE);
        } catch (IOException | EngineServiceException e) {
            throw new ControllerServiceException("Error starting engine service", e);
        }
    }

    public String makeMove(String playerMove) throws ControllerServiceException {
        board.makeMove(playerMove);
        engineService.updateEngineState(boardPrinter.printFEN());

        EngineResponseDTO engineResponse = getEngineResponse();
        board.makeMove(engineResponse.bestMove());

        return boardPrinter.printSVG();
    }

    public EngineResponseDTO getEngineResponse() throws ControllerServiceException {
        try {
            return engineService.getEngineResponse(1000);
        } catch (EngineServiceException e) {
            throw new ControllerServiceException("Error getting response from engine", e);
        }
    }
}
