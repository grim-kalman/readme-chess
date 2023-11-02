package grim.readmechess.webapi.controller;

import grim.readmechess.webapi.service.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chess")
public class Controller {

    private Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/play")
    public ResponseEntity<String> getSelectedSquare(@RequestParam String move) {

        //1. send the request to the service layer to handle the move

        //2. when done return a redirect back to the website

        return ResponseEntity.ok(move);
    }
}
