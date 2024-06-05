package grim.readmechess.controller;

import grim.readmechess.config.AppConfig;
import grim.readmechess.service.chessservice.ChessService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final AppConfig appConfig;
    private final ChessService chessService;

    @GetMapping("/play")
    public RedirectView play(@RequestParam String move, HttpServletRequest request) throws Exception {
        chessService.play(move);
        return new RedirectView(appConfig.getGithubUrl());
    }

    @GetMapping("/select")
    public RedirectView select(@RequestParam String square, HttpServletRequest request) throws Exception {
        chessService.select(square);
        return new RedirectView(appConfig.getGithubUrl());
    }

    @GetMapping("/new")
    public RedirectView newGame(HttpServletRequest request) throws Exception {
        chessService.newGame();
        return new RedirectView(appConfig.getGithubUrl());
    }
}
