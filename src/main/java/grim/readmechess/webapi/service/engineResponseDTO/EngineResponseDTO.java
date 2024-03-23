package grim.readmechess.webapi.service.engineResponseDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EngineResponseDTO(Boolean success, Double evaluation, String bestmove) {
}
