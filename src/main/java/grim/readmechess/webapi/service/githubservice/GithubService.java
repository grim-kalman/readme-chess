package grim.readmechess.webapi.service.githubservice;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GithubService {
    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String REPOS = "/repos/";
    private static final String OWNER = "grim-kalman";
    private static final String REPO = "grim-kalman";
    private static final String README_PATH = "README.md";
    private static final String BRANCH = "main";

    @Value("${github.token}")
    private String token;

    private final ControllerService controllerService;
    private final RestTemplate restTemplate;

    public GithubService(ControllerService controllerService, RestTemplate restTemplate) {
        this.controllerService = controllerService;
        this.restTemplate = restTemplate;
        this.restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    public void updateReadme() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> headersEntity = new HttpEntity<>(headers);
        ObjectMapper mapper = new ObjectMapper();

        JsonNode latestCommitResponse = get(GITHUB_API_URL + REPOS + OWNER + "/" + REPO + "/git/refs/heads/main", HttpMethod.GET, headersEntity);
        String latestCommitSha = latestCommitResponse.get("object").get("sha").asText();

        String newBoardState = controllerService.printBoard();
        Map<String, Object> newTreeMap = new HashMap<>();
        newTreeMap.put("base_tree", latestCommitSha);
        newTreeMap.put("tree", List.of(Map.of("path", README_PATH, "mode", "100644", "type", "blob", "content", newBoardState)));
        String newTreeJson = mapper.writeValueAsString(newTreeMap);

        JsonNode newTreeResponse = post(GITHUB_API_URL + REPOS + OWNER + "/" + REPO + "/git/trees", newTreeJson, headersEntity);
        String newTreeSha = newTreeResponse.get("sha").asText();

        String newCommitJson = "{ \"message\": \"Update README\", \"parents\": [\"" + latestCommitSha + "\"], \"tree\": \"" + newTreeSha + "\" }";
        JsonNode newCommitResponse = post(GITHUB_API_URL + REPOS + OWNER + "/" + REPO + "/git/commits", newCommitJson, headersEntity);
        String newCommitSha = newCommitResponse.get("sha").asText();

        String updateRefJson = "{ \"sha\": \"" + newCommitSha + "\" }";
        restTemplate.exchange(GITHUB_API_URL + REPOS + OWNER + "/" + REPO + "/git/refs/heads/" + BRANCH, HttpMethod.PATCH, new HttpEntity<>(updateRefJson, headers), String.class);
    }

    private JsonNode get(String url, HttpMethod method, HttpEntity<String> entity) throws JsonProcessingException {
        String response = restTemplate.exchange(url, method, entity, String.class).getBody();
        return new ObjectMapper().readTree(response);
    }

    private JsonNode post(String url, String body, HttpEntity<String> headersEntity) throws JsonProcessingException {
        String response = restTemplate.postForEntity(url, new HttpEntity<>(body, headersEntity.getHeaders()), String.class).getBody();
        return new ObjectMapper().readTree(response);
    }
}
