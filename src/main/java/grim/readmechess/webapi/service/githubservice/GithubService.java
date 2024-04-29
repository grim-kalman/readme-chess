package grim.readmechess.webapi.service.githubservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import grim.readmechess.webapi.service.controllerservice.ControllerService;

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
public class GithubService {
    private static final String GITHUB_API_URL = "https://api.github.com/repos";
    private static final String README_PATH = "README.md";
    private static final String BRANCH = "main";

    @Value("${github.owner}")
    private String owner;

    @Value("${github.repo}")
    private String repo;

    @Value("${github.token}")
    private String token;

    private final ControllerService controllerService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GithubService(ControllerService controllerService) {
        this.controllerService = controllerService;
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        this.objectMapper = new ObjectMapper();
    }

    public void updateReadme() throws Exception {
        HttpHeaders headers = createHeaders();

        String latestCommitSha = getLatestCommitSha(headers);
        String newBoardState = controllerService.printBoard();

        String newTreeSha = createNewTree(headers, latestCommitSha, newBoardState);
        String newCommitSha = createNewCommit(headers, latestCommitSha, newTreeSha);

        patch(headers, newCommitSha);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    private String getLatestCommitSha(HttpHeaders headers) throws Exception {
        return get("git/refs/heads/main", headers)
                .path("object")
                .path("sha")
                .asText();
    }

    private String createNewTree(HttpHeaders headers, String latestCommitSha, String newBoardState) throws Exception {
        return post("git/trees", createTreeJson(latestCommitSha, newBoardState), headers)
                .path("sha")
                .asText();
    }

    private String createTreeJson(String latestCommitSha, String newBoardState) throws Exception {
        return new ObjectMapper().writeValueAsString(Map.of(
                "base_tree", latestCommitSha,
                "tree", List.of(Map.of(
                        "path", README_PATH,
                        "mode", "100644",
                        "type", "blob",
                        "content", newBoardState))));
    }

    private String createNewCommit(HttpHeaders headers, String latestCommitSha, String newTreeSha) throws Exception {
        return post("git/commits", createCommitJson(latestCommitSha, newTreeSha), headers)
                .path("sha")
                .asText();
    }

    private String createCommitJson(String latestCommitSha, String newTreeSha) throws Exception {
        return objectMapper.writeValueAsString(Map.of(
                "message", "Update README",
                "parents", List.of(latestCommitSha),
                "tree", newTreeSha));
    }

    private void patch(HttpHeaders headers, String newCommitSha) throws Exception {
        String url = String.format("%s/%s/%s/git/refs/heads/%s", GITHUB_API_URL, owner, repo, BRANCH);
        String body = objectMapper.writeValueAsString(Map.of(
                "sha", newCommitSha));
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
    }

    private JsonNode get(String endpoint, HttpHeaders headers) throws Exception {
        String url = String.format("%s/%s/%s/%s", GITHUB_API_URL, owner, repo, endpoint);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return objectMapper.readTree(responseEntity.getBody());
    }

    private JsonNode post(String endpoint, String body, HttpHeaders headers) throws Exception {
        String url = String.format("%s/%s/%s/%s", GITHUB_API_URL, owner, repo, endpoint);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        return objectMapper.readTree(responseEntity.getBody());
    }
}
