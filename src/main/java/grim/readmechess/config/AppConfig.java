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
    @Value("${app.chess.engine.path}")
    private String enginePath;

    @Value("${app.github.api.url}")
    private String githubApiUrl;

    @Value("${app.github.url}")
    private String githubUrl;

    @Value("${app.github.readme.path}")
    private String readmePath;

    @Value("${app.github.branch}")
    private String branch;

    @Value("${app.github.owner.repo}")
    private String ownerRepo;

    @Value("${app.github.token}")
    private String token;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
