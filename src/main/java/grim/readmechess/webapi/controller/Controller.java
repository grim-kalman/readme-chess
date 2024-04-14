package grim.readmechess.webapi.controller;

import grim.readmechess.webapi.service.controllerservice.ControllerService;
import grim.readmechess.webapi.service.controllerservice.ControllerServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chess")
public class Controller {

    private final ControllerService controllerService;

    public Controller(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping("/play")
    public ResponseEntity<String> makeMove(@RequestParam String move) throws ControllerServiceException {

        //1. send the request to the service layer to handle the move
        String updatedBoard = controllerService.makeMove(move);
        //2. when done return a redirect back to the website

        return ResponseEntity.ok(updatedBoard);
    }
}
