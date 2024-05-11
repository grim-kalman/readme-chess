package grim.readmechess.service.engineservice.dto;

public record EngineResponseDTO(String bestMove, Double evaluation, boolean isGameOver) {
}
