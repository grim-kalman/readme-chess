package grim.readmechess.webapi.controller;

import grim.readmechess.webapi.service.controllerservice.ControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chess")
public class ChessController {

    private final ControllerService controllerService;

    public ChessController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping("/play")
    public ResponseEntity<String> getSelectedSquare(@RequestParam String move) {

        //1. send the request to the service layer to handle the move
        controllerService.makeMove(move);
        //2. when done return a redirect back to the website

        return ResponseEntity.ok(move);
    }
}
