package grim.readmechess.webapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TreeItemResponseDTO(String sha) {
}
