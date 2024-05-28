package grim.readmechess.service.githubservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import grim.readmechess.config.AppConfig;
import grim.readmechess.service.chessservice.ChessService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final AppConfig appConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public void updateReadme(ChessService chessService) throws JsonProcessingException {
        String latestCommitSha = getLatestCommitSha();
        String newBoardState = chessService.printBoard();
        String newTreeSha = createTreeSha(latestCommitSha, newBoardState);
        String newCommitSha = createCommitSha(latestCommitSha, newTreeSha);
        updateRefWithNewCommit(newCommitSha);
    }

    private String getLatestCommitSha() throws JsonProcessingException {
        return handleRequest("git/refs/heads/main", HttpMethod.GET, new HttpEntity<>(createHeaders()))
                .path("object")
                .path("sha")
                .asText();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(appConfig.getToken());
        return headers;
    }

    private JsonNode handleRequest(String endpoint, HttpMethod method, HttpEntity<String> request) throws JsonProcessingException, RestClientException {
        String url = appConfig.getGithubApiUrl() + "/" + appConfig.getOwnerRepo() + "/" + appConfig.getOwnerRepo() + "/" + endpoint;
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, request, String.class);
        return objectMapper.readTree(responseEntity.getBody());
    }

    private String createTreeSha(String latestCommitSha, String newBoardState) throws JsonProcessingException {
        String json = createTreeJson(latestCommitSha, newBoardState);
        return createSha("git/trees", json);
    }

    private String createCommitSha(String latestCommitSha, String newTreeSha) throws JsonProcessingException {
        String json = createCommitJson(latestCommitSha, newTreeSha);
        return createSha("git/commits", json);
    }

    private String createSha(String endpoint, String json) throws JsonProcessingException {
        return handleRequest(endpoint, HttpMethod.POST, new HttpEntity<>(json, createHeaders()))
                .path("sha")
                .asText();
    }

    private String createTreeJson(String latestCommitSha, String newBoardState) throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
                "base_tree", latestCommitSha,
                "tree", List.of(Map.of(
                        "path", appConfig.getReadmePath(),
                        "mode", "100644",
                        "type", "blob",
                        "content", newBoardState))));
    }

    private String createCommitJson(String latestCommitSha, String newTreeSha) throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
                "message", "Update README",
                "parents", List.of(latestCommitSha),
                "tree", newTreeSha));
    }

    private void updateRefWithNewCommit(String newCommitSha) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(Map.of("sha", newCommitSha));
        handleRequest("git/refs/heads/" + appConfig.getBranch(), HttpMethod.PATCH, new HttpEntity<>(json, createHeaders()));
    }
}
