package grim.readmechess.controller;

import grim.readmechess.service.controllerservice.ControllerService;
import grim.readmechess.service.githubservice.GithubService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/chess")
@RequiredArgsConstructor
public class Controller {

    private final ControllerService controllerService;
    private final GithubService githubService;

    @Value("${github.url}")
    private String githubUrl;

    @PostConstruct
    public void initialize() throws Exception {
        githubService.updateReadme();
    }

    @GetMapping("/play")
    public RedirectView play(@RequestParam String move) throws Exception {
        controllerService.play(move);
        githubService.updateReadme();
        return new RedirectView(githubUrl);
    }

    @GetMapping("/select")
    public RedirectView select(@RequestParam String square) throws Exception {
        controllerService.select(square);
        githubService.updateReadme();
        return new RedirectView(githubUrl);
    }
}
