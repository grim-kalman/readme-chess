package grim.readmechess.webapi.controller;

import grim.readmechess.webapi.service.controllerservice.ControllerService;
import grim.readmechess.webapi.service.controllerservice.ControllerServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chess")
public class Controller {

    private final ControllerService controllerService;

    public Controller(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping("/play")
    public ResponseEntity<String> makeMove(@RequestParam String move) throws ControllerServiceException {
        controllerService.makeMove(move);

        return ResponseEntity.ok(controllerService.boardPrinter.printMarkdown());
    }

}
