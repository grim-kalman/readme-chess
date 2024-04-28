package grim.readmechess.webapi.service.githubservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import grim.readmechess.webapi.service.controllerservice.ControllerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GithubService {
    private static final String GITHUB_API_URL = "https://api.github.com/repos/%s/%s";
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

    public GithubService(ControllerService controllerService, RestTemplate restTemplate) {
        this.controllerService = controllerService;
        this.restTemplate = restTemplate;
        this.restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    public void updateReadme() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> headersEntity = new HttpEntity<>(headers);
        ObjectMapper mapper = new ObjectMapper();

        String latestCommitSha = getLatestCommitSha(headersEntity);
        String newBoardState = controllerService.printBoard();

        String newTreeSha = createNewTree(headersEntity, latestCommitSha, newBoardState, mapper);
        String newCommitSha = createNewCommit(headersEntity, latestCommitSha, newTreeSha);

        updateRef(headers, newCommitSha);
    }

    private String getLatestCommitSha(HttpEntity<String> headersEntity) throws Exception {
        JsonNode latestCommitResponse = get(headersEntity);
        return latestCommitResponse.get("object").get("sha").asText();
    }

    private String createNewTree(HttpEntity<String> headersEntity, String latestCommitSha, String newBoardState, ObjectMapper mapper) throws Exception {
        Map<String, Object> newTreeMap = Map.of(
                "base_tree", latestCommitSha,
                "tree", List.of(Map.of("path", README_PATH, "mode", "100644", "type", "blob", "content", newBoardState))
        );
        String newTreeJson = mapper.writeValueAsString(newTreeMap);
        JsonNode newTreeResponse = post("/git/trees", newTreeJson, headersEntity);
        return newTreeResponse.get("sha").asText();
    }

    private String createNewCommit(HttpEntity<String> headersEntity, String latestCommitSha, String newTreeSha) throws Exception {
        String newCommitJson = "{ \"message\": \"Update README\", \"parents\": [\"" + latestCommitSha + "\"], \"tree\": \"" + newTreeSha + "\" }";
        JsonNode newCommitResponse = post("/git/commits", newCommitJson, headersEntity);
        return newCommitResponse.get("sha").asText();
    }

    private void updateRef(HttpHeaders headers, String newCommitSha) {
        String updateRefJson = "{ \"sha\": \"" + newCommitSha + "\" }";
        restTemplate.exchange(String.format(GITHUB_API_URL, owner, repo) + "/git/refs/heads/" + BRANCH, HttpMethod.PATCH, new HttpEntity<>(updateRefJson, headers), String.class);
    }

    private JsonNode get(HttpEntity<String> entity) throws Exception {
        String response = restTemplate.exchange(String.format(GITHUB_API_URL, owner, repo) + "/git/refs/heads/main", HttpMethod.GET, entity, String.class).getBody();
        return new ObjectMapper().readTree(response);
    }

    private JsonNode post(String endpoint, String body, HttpEntity<String> headersEntity) throws Exception {
        String response = restTemplate.postForEntity(String.format(GITHUB_API_URL, owner, repo) + endpoint, new HttpEntity<>(body, headersEntity.getHeaders()), String.class).getBody();
        return new ObjectMapper().readTree(response);
    }
}
