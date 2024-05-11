package grim.readmechess.service.githubservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TreeItemResponseDTO(String sha) {
}
