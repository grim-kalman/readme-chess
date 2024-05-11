package grim.readmechess.controller;

import grim.readmechess.service.controllerservice.ControllerService;
import grim.readmechess.service.githubservice.GithubService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/chess")
public class Controller {

    private final ControllerService controllerService;
    private final GithubService githubService;

    public Controller(ControllerService controllerService, GithubService githubService) {
        this.controllerService = controllerService;
        this.githubService = githubService;
    }

    @GetMapping("/play")
    public RedirectView play(@RequestParam String move) throws Exception {
        controllerService.play(move);
        githubService.updateReadme();
        return new RedirectView("https://github.com/grim-kalman");
    }

    @GetMapping("/select")
    public RedirectView select(@RequestParam String square) throws Exception {
        controllerService.select(square);
        githubService.updateReadme();
        return new RedirectView("https://github.com/grim-kalman");
    }
}
