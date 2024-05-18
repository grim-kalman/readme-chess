package grim.readmechess.service.githubservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import grim.readmechess.service.controllerservice.ControllerService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubService {
    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.readme.path}")
    private String readmePath;

    @Value("${github.branch}")
    private String branch;

    @Value("${github.owner.repo}")
    private String ownerRepo;

    @Value("${github.token}")
    private String token;

    private final ControllerService controllerService;
    private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void updateReadme() throws Exception {
        String latestCommitSha = getLatestCommitSha();
        String newBoardState = controllerService.printBoard();

        String newTreeSha = createTreeSha(latestCommitSha, newBoardState);
        String newCommitSha = createCommitSha(latestCommitSha, newTreeSha);

        updateRefWithNewCommit(newCommitSha);
    }

    private String getLatestCommitSha() throws Exception {
        return handleRequest("git/refs/heads/main", HttpMethod.GET, new HttpEntity<>(createHeaders()))
                .path("object")
                .path("sha")
                .asText();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    private JsonNode handleRequest(String endpoint, HttpMethod method, HttpEntity<String> request) throws Exception {
        String url = githubApiUrl + "/" + ownerRepo + "/" + ownerRepo + "/" + endpoint;
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, request, String.class);
        return objectMapper.readTree(responseEntity.getBody());
    }

    private String createTreeSha(String latestCommitSha, String newBoardState) throws Exception {
        String json = createTreeJson(latestCommitSha, newBoardState);
        return createSha("git/trees", json);
    }

    private String createCommitSha(String latestCommitSha, String newTreeSha) throws Exception {
        String json = createCommitJson(latestCommitSha, newTreeSha);
        return createSha("git/commits", json);
    }

    private String createSha(String endpoint, String json) throws Exception {
        return handleRequest(endpoint, HttpMethod.POST, new HttpEntity<>(json, createHeaders()))
                .path("sha")
                .asText();
    }

    private String createTreeJson(String latestCommitSha, String newBoardState) throws Exception {
        return objectMapper.writeValueAsString(Map.of(
                "base_tree", latestCommitSha,
                "tree", List.of(Map.of(
                        "path", readmePath,
                        "mode", "100644",
                        "type", "blob",
                        "content", newBoardState))));
    }

    private String createCommitJson(String latestCommitSha, String newTreeSha) throws Exception {
        return objectMapper.writeValueAsString(Map.of(
                "message", "Update README",
                "parents", List.of(latestCommitSha),
                "tree", newTreeSha));
    }

    private void updateRefWithNewCommit(String newCommitSha) throws Exception {
        String json = objectMapper.writeValueAsString(Map.of("sha", newCommitSha));
        handleRequest("git/refs/heads/" + branch, HttpMethod.PATCH, new HttpEntity<>(json, createHeaders()));
    }
}
