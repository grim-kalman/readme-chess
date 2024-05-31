package grim.readmechess.controller;

import grim.readmechess.config.AppConfig;
import grim.readmechess.service.chessservice.ChessService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/chess")
@RequiredArgsConstructor
public class Controller {

    private final AppConfig appConfig;
    private final ChessService chessService;

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Application is running");
    }

    @GetMapping("/play")
    public RedirectView play(@RequestParam String move) throws Exception {
        chessService.play(move);
        return new RedirectView(appConfig.getGithubUrl());
    }

    @GetMapping("/select")
    public RedirectView select(@RequestParam String square) throws Exception {
        chessService.select(square);
        return new RedirectView(appConfig.getGithubUrl());
    }

    @GetMapping("/new")
    public RedirectView newGame() throws Exception {
        chessService.newGame();
        return new RedirectView(appConfig.getGithubUrl());
    }
}
