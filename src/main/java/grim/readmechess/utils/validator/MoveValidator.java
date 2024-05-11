package grim.readmechess.utils.validator;

import grim.readmechess.service.engineservice.EngineService;
import org.springframework.stereotype.Component;

@Component
public class MoveValidator {
    private final EngineService engineService;

    public MoveValidator(EngineService engineService) {
        this.engineService = engineService;
    }

    public boolean isValid(String move) {
        return this.engineService.getValidMoves().contains(move);
    }

    public boolean isStartOfValidMove(String position) {
        return this.engineService.getValidMoves().stream()
                .anyMatch(validMove -> validMove.startsWith(position));
    }
}
