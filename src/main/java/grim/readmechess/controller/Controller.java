package grim.readmechess.controller;

import grim.readmechess.config.AppConfig;
import grim.readmechess.service.chessservice.ChessService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final AppConfig appConfig;
    private final ChessService chessService;

    @GetMapping("/play")
    public RedirectView play(@RequestParam String move, HttpServletRequest request) throws Exception {
        log.info("Request to play from IP: {}", request.getRemoteAddr());
        chessService.play(move);
        return new RedirectView(appConfig.getGithubUrl());
    }

    @GetMapping("/select")
    public RedirectView select(@RequestParam String square, HttpServletRequest request) throws Exception {
        log.info("Request to select from IP: {}", request.getRemoteAddr());
        chessService.select(square);
        return new RedirectView(appConfig.getGithubUrl());
    }

    @GetMapping("/new")
    public RedirectView newGame(HttpServletRequest request) throws Exception {
        log.info("Request to start new game from IP: {}", request.getRemoteAddr());
        chessService.newGame();
        return new RedirectView(appConfig.getGithubUrl());
    }
}
