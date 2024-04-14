package grim.readmechess.webapi.service.controllerservice;

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

    public String getMoveFromEngine() throws ControllerServiceException {
        try {
            return engineService.getBestMove(1000);
        } catch (IOException | EngineServiceException e) {
            throw new ControllerServiceException("Error getting move from engine", e);
        }
    }
}
