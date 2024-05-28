package grim.readmechess.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class AppConfig {
    @Value("${chess.engine.path:#{systemEnvironment['chess.engine.path']}}")
    private String enginePath;

    @Value("${github.api.url:#{systemEnvironment['github.api.url']}}")
    private String githubApiUrl;

    @Value("${github.url:#{systemEnvironment['github.url']}}")
    private String githubUrl;

    @Value("${github.readme.path:#{systemEnvironment['github.readme.path']}}")
    private String readmePath;

    @Value("${github.branch:#{systemEnvironment['github.branch']}}")
    private String branch;

    @Value("${github.owner.repo:#{systemEnvironment['github.owner.repo']}}")
    private String ownerRepo;

    @Value("${github.token:#{systemEnvironment['github.token']}}")
    private String token;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
