package grim.readmechess.webapi.dto;

public record EngineResponseDTO(String bestMove, Double evaluation, boolean isGameOver) {
}
