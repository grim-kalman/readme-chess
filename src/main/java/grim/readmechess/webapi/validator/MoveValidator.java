package grim.readmechess.webapi.validator;

import grim.readmechess.webapi.service.engineservice.EngineService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MoveValidator {
    private final EngineService engineService;

    public MoveValidator(EngineService engineService) {
        this.engineService = engineService;
    }

    public boolean isValid(String move) {
        return this.engineService.getValidMoves().contains(move);
    }
}
