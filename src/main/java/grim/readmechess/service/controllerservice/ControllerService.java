package grim.readmechess.service.controllerservice;

import grim.readmechess.service.engineservice.dto.EngineResponseDTO;
import grim.readmechess.model.chessboard.Board;
import grim.readmechess.utils.printer.FenBoardPrinter;
import grim.readmechess.utils.printer.MarkdownBoardPrinter;
import grim.readmechess.service.engineservice.EngineService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ControllerService {

    private final Board board;
    private final FenBoardPrinter fenBoardPrinter;
    private final MarkdownBoardPrinter markdownBoardPrinter;
    private final EngineService engineService;

    public void play(String move) {
        EngineResponseDTO response = makeMove(move);
        if (response.isGameOver()) {
            // TODO: Implement banter and trashtalk.
            System.out.println("Game over");
        }

        response = makeMove(getEngineResponse().bestMove());
        if (response.isGameOver()) {
            System.out.println("Game over");
        }
    }

    public void select(String square) {
        board.selectSquare(square);
    }

    public void newGame() {
        board.reset();
        engineService.updateEngineState(fenBoardPrinter.print());
    }

    public String printBoard() {
        return markdownBoardPrinter.print();
    }

    private EngineResponseDTO getEngineResponse() {
        return engineService.getEngineResponse();
    }

    private EngineResponseDTO makeMove(String move) {
        board.makeMove(move);
        engineService.updateEngineState(fenBoardPrinter.print());
        return engineService.getEngineResponse();
    }
}
