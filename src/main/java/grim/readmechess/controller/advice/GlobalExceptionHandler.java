package grim.readmechess.controller.advice;

import grim.readmechess.config.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final AppConfig appConfig;

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public RedirectView handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid move: {}", e.getMessage());
        return new RedirectView(appConfig.getGithubUrl());
    }

    @ExceptionHandler(value = {NoResourceFoundException.class})
    public RedirectView handleInvalidEndpointRequest(NoResourceFoundException e) {
        log.error("Invalid endpoint: {}", e.getMessage());
        return new RedirectView(appConfig.getGithubUrl());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception e) {
        log.error("Internal error: {}", e.getMessage());
        return new ResponseEntity<>("Internal error, see the logs for more information", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
